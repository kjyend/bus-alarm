package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.BusDto;
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
public class BusController {

    private String iddd;//노선 id
    private String dkdk;//노선 id, 정류소 id, 노선의 정류소 순번

    @GetMapping("bus")//값을 보내야한다. 아두이노로
    public String areaForm(BusDto busDto, AreaDto areaDto, Model model) throws IOException, ParserConfigurationException, SAXException {
        model.addAttribute("bus",busDto);
        busLiveApi();//노선 id를 넣는다. -이거 흠
        busLive();//노선id, -버스의 현재위치와+번스 번호+내리는 역을 입력받는다.
        return "bus/BusLive";
    }
    @PostMapping("bus")
    public String areaData(){
        return "redirect:";
    }

    public String busLiveApi() throws IOException, ParserConfigurationException, SAXException {// 실시간 버스 위치 +노선도+json을 아두이노한테 보내주어야 한다.(버스 위치정보 조회-실시간으로 버스위치 알수있다.)
        //변수 -int nodeId
        //노선 id를 넣고 버스위치 정보 목록을 살펴야한다.

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/buslocationservice/getBusLocationList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode("233000031", "UTF-8")); /*노선 ID*/
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
        log.info("men={}",sb);
        //빌더 팩토리 생성
        DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();

        //빌더 팩토리로부터 빌더 생성
        DocumentBuilder builder=builderFactory.newDocumentBuilder();

        //빌더를 통해 xml 문서를 파싱해서 Document 객체로 가져온다.
        Document document=builder.parse(String.valueOf(url));

        // 문서 구조 안정화
        document.getDocumentElement().normalize();


        NodeList nodeList= document.getElementsByTagName("busLocationList");

        NodeList childNodes=nodeList.item(0).getChildNodes();
        for(int j=0;j<childNodes.getLength();j++){
            if("endBus".equals(childNodes.item(j).getNodeName())){
                log.info("131={}",childNodes.item(j).getAttributes());
            }
            if("lowPlate".equals(childNodes.item(j).getNodeName())){
                log.info("232={}",childNodes.item(j).getChildNodes());
            }
            if("plateNo".equals(childNodes.item(j).getNodeName())){
                log.info("333={}",childNodes.item(j).getFirstChild());//값이 나온다.-key-value같이 나옴
            }
            if("routeId".equals(childNodes.item(j).getNodeName())){
                log.info("434={}",childNodes.item(j).getLastChild());//값이 나온다.-key-value같이 나옴
            }
            if("stationId".equals(childNodes.item(j).getNodeName())){
                log.info("535={}",childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
            }
        }

        return "노선아이디,정류소 아이디, 정류소 순번을 받는다.";
    }

    //버스 도착 정보 항목 조회 (노선 아이디)넣고-(x,y좌표, stationSeq-순번,stationName-역 이름)//이걸이용해서 잘쓰기
    //+버스도착정보항목조회
    public void busLive() throws IOException, SAXException, ParserConfigurationException {
        //int nodeId
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
        
        //1개밖에없음
        NodeList nodeList= document.getElementsByTagName("busRouteStationList");

        NodeList childNodes=nodeList.item(0).getChildNodes();
        for(int j=0;j<childNodes.getLength();j++){
            if("turnYn".equals(childNodes.item(j).getNodeName())){
                log.info("141={}",childNodes.item(j).getAttributes());
            }
            if("stationSeq".equals(childNodes.item(j).getNodeName())){
                log.info("242={}",childNodes.item(j).getChildNodes());
            }
            if("stationId".equals(childNodes.item(j).getNodeName())){
                log.info("343={}",childNodes.item(j).getFirstChild());//값이 나온다.-key-value같이 나옴
            }
            if("stationName".equals(childNodes.item(j).getNodeName())){
                log.info("444={}",childNodes.item(j).getLastChild());//값이 나온다.-key-value같이 나옴
            }
            if("x".equals(childNodes.item(j).getNodeName())){
                log.info("545={}",childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
            }
        }

    }

}
