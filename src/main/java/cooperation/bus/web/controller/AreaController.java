package cooperation.bus.web.controller;

import cooperation.bus.domain.service.AreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    private final AreaService areaService;

    @GetMapping("area")//다른값을 뽑아낸다. 버스의 번호를 저장하는게 목표이다.
    public String areaForm() throws IOException {
        busApi();
        busStation();
        return "bus/BusSetting";
    }

    public StringBuilder busStation() throws IOException{//정류장 위치 확인 x,y값을 넣어서 다양한 값들얻는다, x,y값을 넣어서 정류소Id를 빼넨다.
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busstationservice/getBusStationAroundList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=SOLuYRh8xqz5eiyULHRGa7argcZ5hB4drsGC1LFh91Og5tZwMs4Jk34TctQelxAph%2BlwkFPoh%2F9oAcB0XM8PHQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("x","UTF-8") + "=" + URLEncoder.encode("127.0284667", "UTF-8")); /*X 좌표(WGS84)*/
        urlBuilder.append("&" + URLEncoder.encode("y","UTF-8") + "=" + URLEncoder.encode("37.49545", "UTF-8")); /*Y 좌표(WGS84)*/
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
        return sb;
    }

    public StringBuilder busApi()throws IOException {// 정류소경유노선 목록조회 (정확히 몇번 버스로 설정할것인지 판단+ 지도를 찍어서 주변 정류소+지나는 버스값을 얻는다.)
        //정류소Id를 가지고 노선 목록과 노선 아이디를 보넨다.

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
        return sb; //sb값이 xml값이다.
    }
}
