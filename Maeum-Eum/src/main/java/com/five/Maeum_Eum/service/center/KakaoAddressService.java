package com.five.Maeum_Eum.service.center;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoAddressService {

    private String API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";
    @Value("${kakao.address.api-key}")
    private String API_KEY;  // 🔹 여기에 본인의 API 키 입력

    public Map<String, Object> getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = API_URL + address;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}
