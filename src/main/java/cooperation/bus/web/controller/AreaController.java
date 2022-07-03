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

@Controller
@RequiredArgsConstructor
@Slf4j//반응형웹을 만들어야한다. 근데 안만들어진다 또한 값을 통일해주어야한다. 설계에 대하여 다시 고민해야할듯하다.
public class AreaController {

    //repository 
    //service를 사용해서 원하는값 얻기
    //시리얼통신을 통해서 outㅇ을한다. 끝 마무리한다.
    //html에서 버스 어느 지역에 있는지 봐야하느데 조회하는거 0.스프링입문,34페이지 조회에서 본다.
    
    private final AreaService areaService;

    @GetMapping("area")
    public String areaForm(AreaDto areaDto, Model model) throws IOException, ParserConfigurationException, SAXException {
        //rxtx(시리얼통신)를 하는것도 생각해봐야한다. outstream으로 가능할것같다
        busStation();//버스정류장역 이름을 적는다.
        busApi();//정류소id을 넣는다.- 노선ID를 꺼낸다.
        model.addAttribute("area",areaDto);
        return "bus/BusStop";
    }

    @PostMapping("area")
    public String areaData(AreaDto areaDto) throws IOException, ParserConfigurationException, SAXException {
        return "redirect:";
    }

    public String busStation() throws IOException, ParserConfigurationException, SAXException {//주변정류소 목록조회+경기도_정류소 조회
        //변수-String name
        //정류소명/번호 목록조회= 버스역을 적으면 값을 준다.
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busstationservice/getBusStationList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("keyword","UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*정류소명 또는 번호(2자리 이상)*/
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

        NodeList nodeList = document.getElementsByTagName("busStationList");

        for(int i=0;i<nodeList.getLength();i++){
            NodeList childNodes = nodeList.item(i).getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if ("centerYn".equals(childNodes.item(j).getNodeName())) {
                    log.info("111={}", childNodes.item(j).getAttributes());
                }
                if ("districtCd".equals(childNodes.item(j).getNodeName())) {
                    log.info("212={}", childNodes.item(j).getChildNodes());
                }
                if ("stationName".equals(childNodes.item(j).getNodeName())) {
                    log.info("313={}", childNodes.item(j).getFirstChild());//값이 나온다.-key-value같이 나옴
                }
                if ("x".equals(childNodes.item(j).getNodeName())) {
                    log.info("414={}", childNodes.item(j).getLastChild());//값이 나온다.-key-value같이 나옴
                }
                if ("stationId".equals(childNodes.item(j).getNodeName())) {
                    log.info("515={}", childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
                }
            }
        }

        return "정류소 ID를 넣기";
    }

    public String busApi() throws IOException, ParserConfigurationException, SAXException {// 정류소경유노선 목록조회 (정확히 몇번 버스로 설정할것인지 판단+ 지도를 찍어서 주변 정류소+지나는 버스값을 얻는다.)
        //정류소Id를 가지고 노선 목록과 노선 아이디를 보넨다. +경기도 정류소 조회
        //변수 -int busId

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busstationservice/getBusStationViaRouteList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode("233001450", "UTF-8")); /*정류소ID*/

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


        NodeList nodeList= document.getElementsByTagName("busRouteList");

        NodeList childNodes=nodeList.item(0).getChildNodes();
        for(int j=0;j<childNodes.getLength();j++){
            if("regionName".equals(childNodes.item(j).getNodeName())){
                log.info("121={}",childNodes.item(j).getAttributes());
            }
            if("routeTypeName".equals(childNodes.item(j).getNodeName())){
                log.info("222={}",childNodes.item(j).getChildNodes());
            }
            if("routeName".equals(childNodes.item(j).getNodeName())){
                log.info("323={}",childNodes.item(j).getFirstChild());//값이 나온다.-key-value같이 나옴
            }
            if("routeId".equals(childNodes.item(j).getNodeName())){
                log.info("424={}",childNodes.item(j).getLastChild());//값이 나온다.-key-value같이 나옴
            }
            if("routeTypeCd".equals(childNodes.item(j).getNodeName())){
                log.info("525={}",childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
            }
        }

        return "노선 ID넣는다."; //sb값이 xml값이다.
    }


}
