package mhl.gochariot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class TransitController {
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    String url = "https://store.transitstat.us/passio_go/uncg/";

    public JsonNode requestTransitAPI(String path) throws JsonProcessingException {
        String endpoint = url + path;

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Contact: log4shton@proton.me | github.com/logashton");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);

        String jsonInfo = response.getBody();
        JsonNode root = mapper.readTree(jsonInfo);

        System.out.println("JSON ROOT: " + root);

        return root;
    }



    // path can be trains, lines, or stops
    @GetMapping("/api/transits")
    public ResponseEntity<?> requestTransits(@RequestParam(required = false) String path) {
        if (path == null || path.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Path parameter is missing");
        }

        try {
            return ResponseEntity.ok(requestTransitAPI(path));
        } catch (JsonProcessingException e) {
            System.out.println("Error in /api/transits/trains: " + e);
            return ResponseEntity.badRequest().body("Error finding transits");
        }

    }



}
