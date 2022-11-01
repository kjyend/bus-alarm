package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.service.AreaService;
import cooperation.bus.domain.service.BusService;
import cooperation.bus.domain.service.SerialService;
import cooperation.bus.web.argumentresolver.Login;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
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

    private final BusService busService;
    private final AreaService areaService;
    private final SerialService serial;

    String busNumber=null;
    String startTime =null;
    String endTime =null;

    @GetMapping("area")
    public String areaForm(@Login MemberDto loginMember, Model model) throws IOException, ParserConfigurationException, SAXException {
        //rxtx(시리얼통신)를 하는것도 생각해봐야한다. outstream으로 가능할것같다
        //busrepository로 find하고 member값하고 areaRepository를 통해서 원하는 값을 얻는다.
        //정류소 비교해서 원하는 시간을 얻어야한다.
        //이제 html에서 데이터를 보는역활을 해야하낟.
        if(startTime!=null){
            startTime=null;
            endTime=null;
        }

        String stationId = areaService.findStationId(loginMember.getLoginId());
        String stopId = areaService.findStopId(loginMember.getLoginId());

        //busservice에서 사용해야한다.노드 id를
        String busNode = busService.nodeFind(loginMember.getLoginId());

        String busNumber = busService.numberFind(loginMember.getLoginId());
        log.info("222={}",busNode);

        startTime = busStation(stationId, busNode);
        if(startTime==null){
            startTime = "버스가 없습니다.";
            endTime= "버스가 없습니다.";
        }else {
            endTime = busStation(stopId, busNode);
        }
        if(endTime==null){
            endTime = "버스가 없습니다.";
        }
        AreaDto area = areaService.findArea(loginMember.getLoginId());

        log.info("123={}",area.getBusStationName());
        log.info("333={}",startTime);
        //startTime=null임, startstation이 갑자기 안낭온다.
        //null값이 나온다면 다른 답으로 나오게 해야한다. ex) 버스 없음
        //도착 역에서 시간을 받아야하는데 받을수 있다.
        // 버스 번호 값을 추출하기 함

        model.addAttribute("busNumber", busNumber);
        model.addAttribute("startStation",area.getBusStationName());
        model.addAttribute("stopStation",area.getBusStopName());
        model.addAttribute("startTime",startTime);
        model.addAttribute("endTime",endTime);

        return "bus/BusData";
    }

    @PostMapping("area")
    public String areaData(BusDto busDto, AreaDto areaDto) throws UnsupportedCommOperationException, NoSuchPortException, PortInUseException, IOException {

        if(startTime.equals("버스가 없습니다.")){
            startTime="0";
            endTime="0";
            serial.connect("COM3", "0", startTime, endTime);
        }else {
            serial.connect("COM3", busDto.getBusNumber(), startTime, endTime);
        }
        return "redirect:";
    }

    public String busStation(String stopId,String nodeId) throws IOException, ParserConfigurationException, SAXException {
        //경기도_버스도착정보 조회+버스도착정보목록조회
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode(stopId, "UTF-8")); /*정류소ID*/
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
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        //빌더 팩토리로부터 빌더 생성
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        //빌더를 통해 xml 문서를 파싱해서 Document 객체로 가져온다.
        Document document = builder.parse(String.valueOf(url));

        // 문서 구조 안정화
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("busArrivalList");

        String nextTime = null;

        if(startTime==null){
            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList childNodes = nodeList.item(i).getChildNodes();
                if (nodeId.equals(childNodes.item(11).getTextContent())) {
                    busNumber = childNodes.item(5).getTextContent();
                    nextTime = childNodes.item(7).getTextContent();
                }
            }
        }else {
            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList childNodes = nodeList.item(i).getChildNodes();
                if (nodeId.equals(childNodes.item(11).getTextContent())) {
                    if(busNumber.equals(childNodes.item(5).getTextContent())){
                        nextTime = childNodes.item(7).getTextContent();
                    }else if(busNumber.equals(childNodes.item(6).getTextContent())){
                        nextTime=childNodes.item(8).getTextContent();
                    }else {
                        nextTime="시간을 구하지 못했습니다.";
                    }

                }
            }
        }

        return nextTime;
    }

}
