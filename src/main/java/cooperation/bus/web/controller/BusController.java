package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.BusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@Slf4j
@Controller
public class BusController {
    //rxtx(시리얼통신)를 하는것도 생각해봐야한다. outstream으로 가능할것같다
    @GetMapping("bus")//값을 보내야한다. 아두이노로
    public String areaForm(BusDto busDto, AreaDto areaDto, Model model) throws IOException {
        model.addAttribute("bus",busDto);
        busLiveApi();//areaDto로 원래가지고 있던 정보를 주어야한다. 그리고 busdto를 넣어서 각종아이디를 넣어야한다.
        busLive();
        return "bus/BusLive";
    }
    @PostMapping("bus")
    public String areaData(){
        return "redirect:";
    }

    public String busLiveApi() throws IOException {// 실시간 버스 위치 +노선도+json을 아두이노한테 보내주어야 한다.(버스 위치정보 조회-실시간으로 버스위치 알수있다.)
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
        log.info("member={}",sb);
        return "노선아이디,정류소 아이디, 정류소 순번을 받는다.";
    }

    //버스 도착 정보 항목 조회 (정류소 아이디, 노선 아이디, 정류소 순번)넣고-busapi를 통해서 값을 얻고 값 출력
    public void busLive() throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalItem"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode("200000177", "UTF-8")); /*정류소ID*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode("200000037", "UTF-8")); /*노선ID*/
        urlBuilder.append("&" + URLEncoder.encode("staOrder","UTF-8") + "=" + URLEncoder.encode("19", "UTF-8")); /*노선의 정류소 순번*/
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
    }

}
