package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("setting")
    public String setForm(@Login MemberDto loginMember) throws IOException, ParserConfigurationException, SAXException { //경기도_버스노선 조회- 노선번호 목록조회, 노선번호 넣고 노선id를 얻는다.
        busNumber();
        return "bus/BusSetting";
    }

    @PostMapping("setting")
    public String setData(){
        return "redirect:";
    }

    public void busNumber() throws IOException, ParserConfigurationException, SAXException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode("11", "UTF-8")); /*노선번호*/
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

        NodeList childNodes = nodeList.item(0).getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++) {
            if ("districtCd".equals(childNodes.item(j).getNodeName())) {
                log.info("131={}", childNodes.item(j).getAttributes());
            }
            if ("routeTypeName".equals(childNodes.item(j).getNodeName())) {
                log.info("232={}", childNodes.item(j).getChildNodes());
            }
            if ("routeName".equals(childNodes.item(j).getNodeName())) {
                log.info("333={}", childNodes.item(j).getFirstChild());//값이 나온다.-key-value같이 나옴
            }
            if ("regionName".equals(childNodes.item(j).getNodeName())) {
                log.info("434={}", childNodes.item(j).getLastChild());//값이 나온다.-key-value같이 나옴
            }
            if ("routeId".equals(childNodes.item(j).getNodeName())) {
                log.info("535={}", childNodes.item(j).getTextContent());//값이 나온다.-value만나옴
            }
        }
    }
}
