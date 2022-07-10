package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.service.BusService;
import cooperation.bus.domain.service.MemberService;
import cooperation.bus.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SettingController {

    private final BusService busService;

    @GetMapping("setting")//memberid값을 넣어야한다. 그리고 그 값으로 post를 저장해야한다. 그 값을 비교해서 노드값을 해서 웒하는값을 넣어야한다.
    public String setForm(@Login MemberDto loginMember, @RequestParam("busSearch") String busNumber, BusDto busDto, Model model) throws IOException, ParserConfigurationException, SAXException {
        //경기도_버스노선 조회- 노선번호목록조회ㄹ.) (그리고 현 위치를 알아내고 전 라인을 알아내는걸 봐야한다., 노선번호 넣고 노선id를 얻는다.
        // bus와 member연동해서 member와 연동한 busnum값을 얻고 findBusNum를 통해서 미리 값을 얻는다.

        log.info("memberLogin11={}",loginMember.getLoginId());
        String[][] busData = busNumber(busNumber);//노선목록을 쭉 세워두고 하나를 선택하게 한다.
        model.addAttribute("bus",busData);
        return "bus/BusSetting";
    }

    @PostMapping("setting") //지금 일단 사용안한다.
    public String setData(@Login MemberDto loginMember,BusDto busDto){//데이터를 저장이 안됨 일단 대기한다.

        log.info("memberLogin222={}",loginMember.getLoginId());
        log.info("memberLogin222={}",loginMember.getBus());
        log.info("logId={}",busDto.getBusId());
        log.info("logArea={}",busDto.getBusArea());
        log.info("logNumber={}",busDto.getBusNumber());

        busService.busSave(busDto);
        return "redirect:";
    }

    public String[][] busNumber(String busNum) throws IOException, ParserConfigurationException, SAXException {//노선이름 적고 얻어온다.
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(busNum, "UTF-8")); /*노선번호*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        //빌더 팩토리로부터 빌더 생성
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        //빌더를 통해 xml 문서를 파싱해서 Document 객체로 가져온다.
        Document document = builder.parse(String.valueOf(url));

        // 문서 구조 안정화
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("busRouteList");
        String[][] arr=new String[nodeList.getLength()][3];

        for(int temp=0;temp<nodeList.getLength();temp++){
            NodeList childNodes = nodeList.item(temp).getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if ("regionName".equals(childNodes.item(j).getNodeName())) {
                    log.info("1={}", childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
                    arr[temp][0] = childNodes.item(j).getTextContent();
                }
                if ("routeId".equals(childNodes.item(j).getNodeName())) {
                    log.info("2={}", childNodes.item(j).getTextContent());//값이 나온다.-key-value같이 나옴
                    arr[temp][1] = childNodes.item(j).getTextContent();
                }
                if ("routeName".equals(childNodes.item(j).getNodeName())) {
                    log.info("3={}", childNodes.item(j).getTextContent());//값이 나온다.-key-value같이 나옴
                    arr[temp][2] = childNodes.item(j).getTextContent();
                }
            }
        }
        return arr;
    }
}
