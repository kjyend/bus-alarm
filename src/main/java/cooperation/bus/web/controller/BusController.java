package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.service.AreaService;
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


@Slf4j
@Controller
@RequiredArgsConstructor
public class BusController {

    private final AreaService areaService;
    private static String[][] busRouteArr;

    @GetMapping("bus")//노드값을 받아야한다.+ 방향성을 선택해야한다.
    public String busForm(AreaDto areaDto, Model model) throws IOException, ParserConfigurationException, SAXException {
        busRouteArr= busRoute("123");//busdto에있는 값을 넣줘야한다.
        model.addAttribute("busRoute",busRouteArr);
        return "bus/BusLive";
    }
    @PostMapping("bus")
    public String areaData(){
        return "redirect:";
    }

    @GetMapping("busStop")
    public String busStopForm(@Login MemberDto loginMember, AreaDto areaDto, Model model){
        model.addAttribute("busStationId",areaDto.getBusStationId());
        model.addAttribute("busStationName",areaDto.getBusStationName());
        model.addAttribute("busRoute", busRouteArr);

        return "bus/BusStop";
    }
    @PostMapping("busStop")
    public String busStopData(AreaDto areaDto){
        areaService.areaSave(areaDto);
        return "redirect:";
    }

    public String[][] busRoute(String routeId) throws IOException, ParserConfigurationException, SAXException {//routeId 노선id이다.
        //1. 경기 버스 노선 조회- 2. 경유정류소목록조회 -(노선id넣고 정보 빼자 다뺄듯)
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteStationList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode("200000085", "UTF-8")); /*노선ID*/
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

        //빌더 팩토리 생성
        DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();

        //빌더 팩토리로부터 빌더 생성
        DocumentBuilder builder=builderFactory.newDocumentBuilder();

        //빌더를 통해 xml 문서를 파싱해서 Document 객체로 가져온다.
        Document document=builder.parse(String.valueOf(url));

        // 문서 구조 안정화
        document.getDocumentElement().normalize();


        NodeList nodeList= document.getElementsByTagName("busRouteStationList");
        String[][] arr=new String[nodeList.getLength()][2];//

        for(int temp=0;temp<nodeList.getLength();temp++){
            NodeList childNodes=nodeList.item(temp).getChildNodes();
            for(int j=0;j<childNodes.getLength();j++) {
                if ("stationId".equals(childNodes.item(j).getNodeName())) {
                    log.info("1={}", childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
                    arr[temp][0] = childNodes.item(j).getTextContent();
                }
                if ("stationName".equals(childNodes.item(j).getNodeName())) {
                    log.info("1={}", childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
                    arr[temp][1] = childNodes.item(j).getTextContent();
                }
            }
        }

        return arr;
    }

}
