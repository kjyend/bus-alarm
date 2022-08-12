package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.service.AreaService;
import cooperation.bus.domain.service.BusService;
import cooperation.bus.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AreaController {

    //repository 
    //service를 사용해서 원하는값 얻기
    //시리얼통신을 통해서 outㅇ을한다. 끝 마무리한다.
    //html에서 버스 어느 지역에 있는지 봐야하느데 조회하는거 0.스프링입문,34페이지 조회에서 본다.
    
    //시리얼통신을 이용하여 아두이노외 통신을 해야한다.
    //표시는 시작역 , 도착역, 버스번호, 각역의 도착시간을 해야한다. ㅁ
    //find값을 통해서 각각의 원하는 값을 얻어야한다. 다음으로


    private final AreaService areaService;

    @GetMapping("area")
    public String areaForm(@Login MemberDto loginMember, AreaDto areaDto, Model model) throws IOException, ParserConfigurationException, SAXException {
        //rxtx(시리얼통신)를 하는것도 생각해봐야한다. outstream으로 가능할것같다
        //busrepository로 find하고 member값하고 areaRepository를 통해서 원하는 값을 얻는다.
        //정류소 비교해서 원하는 시간을 얻어야한다.

        String stationId = areaService.CompareStationId(loginMember.getLoginId());

        busStation(stationId);//버스정류장역 이름을 적는다.
        log.info("123={}",areaDto.getBusStationName());
        model.addAttribute("area",areaDto);
        return "bus/BusData";
    }

    @PostMapping("area")
    public String areaData(AreaDto areaDto) throws IOException, ParserConfigurationException, SAXException {
        //데이터를 보내야하는데 1.시작점 도착 버스 시간, 2.도착점 도착 버스 시간, 3.버스 번호
        return "redirect:";
    }

    public String busStation(String id) throws IOException, ParserConfigurationException, SAXException {//경기도_버스도착정보 조회+버스도착정보목록조회
        //변수-String name
        //정류소명/번호 목록조회= 버스역을 적으면 값을 준다.
        //정류소 id값을 비교해서 노선id를 넣어서 정류소 id값을 비교해서 시간값을 얻어야한다.
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode("200000078", "UTF-8")); /*정류소ID*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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

        //빌더 팩토리 생성
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        //빌더 팩토리로부터 빌더 생성
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        //빌더를 통해 xml 문서를 파싱해서 Document 객체로 가져온다.
        Document document = builder.parse(String.valueOf(url));

        // 문서 구조 안정화
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("busArrivalList");

        for(int i=0;i<nodeList.getLength();i++){
            NodeList childNodes = nodeList.item(i).getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if ("routeId".equals(childNodes.item(j).getNodeName())) {
                    log.info("111={}", childNodes.item(j).getAttributes());
                }
                if ("stationId".equals(childNodes.item(j).getNodeName())) {
                    log.info("212={}", childNodes.item(j).getChildNodes());
                }
                if ("staOrder".equals(childNodes.item(j).getNodeName())) {
                    log.info("313={}", childNodes.item(j).getFirstChild());//값이 나온다.-key-value같이 나옴
                }
                if ("locationNo1".equals(childNodes.item(j).getNodeName())) {//정류소 얼마나 남았는가
                    log.info("414={}", childNodes.item(j).getLastChild());//값이 나온다.-key-value같이 나옴
                }
                if ("predictTime1".equals(childNodes.item(j).getNodeName())) {//정류소에 얼마나 뒤에 오는가
                    log.info("515={}", childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
                }
            }
        }

        return "데이터를 보내야한다.";
    }




}
