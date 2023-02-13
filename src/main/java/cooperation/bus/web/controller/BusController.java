package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.service.AreaService;
import cooperation.bus.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
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
public class BusController {

    private final AreaService areaService;
    private static String[][] busRouteArr;

    @GetMapping("bus/{busNodeId}")//노드값을 받아야한다.+ 방향성을 선택해야한다.
    public String busForm(BusDto busDto, Model model) throws IOException, ParserConfigurationException, SAXException {
        //노선 번호를 얻어야한다. 노선 번호를 얻으려면 다른 방ㅅ식으로 보내거나 find값으로 찾자
        busRouteArr= busRoute(busDto.getBusNodeId());//busdto에있는 값을 넣줘야한다.
        model.addAttribute("busRoute",busRouteArr);
        return "bus/BusLive";
    }
    @GetMapping("bus/busStop")
    public String busStopForm(@Login MemberDto loginMember, Model model){

        model.addAttribute("busRoute", busRouteArr);

        return "bus/BusStop";
    }
    @PostMapping("bus/busStop")
    public String busStopData(@Login MemberDto loginMember, AreaDto areaDto){
        areaService.areaSave(areaDto,loginMember.getLoginId());
        return "redirect:";
    }

    public String[][] busRoute(String routeId) throws IOException, ParserConfigurationException, SAXException {
        //1. 경기 버스 노선 조회- 경유정류소목록조회
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteStationList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8")); /*노선ID*/
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
                    arr[temp][0] = childNodes.item(j).getTextContent();
                }
                if ("stationName".equals(childNodes.item(j).getNodeName())) {
                    arr[temp][1] = childNodes.item(j).getTextContent();
                }
            }
        }

        return arr;
    }

}
