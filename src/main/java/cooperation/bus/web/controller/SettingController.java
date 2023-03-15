package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.service.BusService;
import cooperation.bus.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

@Controller
@RequiredArgsConstructor
public class SettingController {

    @Value("${bus.route.list.key}")
    String key;

    private final BusService busService;

    @GetMapping("setting")
    public String setForm(@Login MemberDto loginMember, @RequestParam("busSearch") String busNumber, Model model) throws IOException, ParserConfigurationException, SAXException {
        //경기도_버스노선 조회- 노선번호목록조회

        String[][] busData = busNumber(busNumber);//노선목록을 쭉 세워두고 하나를 선택하게 한다.
        model.addAttribute("bus",busData);
        model.addAttribute("login",loginMember);
        return "bus/BusSetting";
    }

    @PostMapping("setting")
    public String setData(@Login MemberDto loginMember, BusDto busDto, RedirectAttributes redirectAttributes){
        busService.busSave(busDto,loginMember.getLoginId());
        redirectAttributes.addAttribute("busNodeId", busDto.getBusNodeId());
        return "redirect:/bus/{busNodeId}";
    }

    public String[][] busNumber(String busNum) throws IOException, ParserConfigurationException, SAXException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + key); /*Service Key*/
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

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(String.valueOf(url));
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("busRouteList");
        String[][] arr=new String[nodeList.getLength()][3];

        for(int temp=0;temp<nodeList.getLength();temp++){
            NodeList childNodes = nodeList.item(temp).getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if ("regionName".equals(childNodes.item(j).getNodeName())) {
                    arr[temp][0] = childNodes.item(j).getTextContent();
                }
                if ("routeId".equals(childNodes.item(j).getNodeName())) {
                    arr[temp][1] = childNodes.item(j).getTextContent();
                }
                if ("routeName".equals(childNodes.item(j).getNodeName())) {
                    arr[temp][2] = childNodes.item(j).getTextContent();
                }
            }
        }
        return arr;
    }
}
