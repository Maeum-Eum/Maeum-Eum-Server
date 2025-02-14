package com.five.Maeum_Eum.service.center;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAddressService {

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final RestTemplate restTemplate = new RestTemplate();
    private String API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";
    @Value("${kakao.address.api-key}")
    private String API_KEY;  // 🔹 여기에 본인의 API 키 입력

    public Point getCoordinates(String address) { // 도로명 주소를 받아서 Point 객체의 좌표로 반환

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = API_URL + address;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("documents")) {
            List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");
            if (!documents.isEmpty()) {
                Map<String, Object> firstResult = documents.get(0);
                double longitude = Double.parseDouble(firstResult.get("x").toString());
                double latitude = Double.parseDouble(firstResult.get("y").toString());

                Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
                point.setSRID(4326);

                return point;
            }
        }

        return null;
    }
}
