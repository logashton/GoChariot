package mhl.gochariot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class BusController {
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    // not sure what the point of deviceId is tbh
    String url = "https://passiogo.com/mapGetData.php?deviceId=45567185";

    public JsonNode requestBusAPI(String endpoint) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        // might change this later to just use a randomly generated user-agent
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        headers.set("Origin", "https://passiogo.com");
        headers.set("Referer", "https://passiogo.com/");
        headers.setContentType(MediaType.APPLICATION_JSON);


        String jsonData = "{\"s0\":\"2874\",\"sA\":1}";

        HttpEntity<String> entity = new HttpEntity<>(jsonData, headers);

        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);

        String jsonInfo = response.getBody();
        JsonNode root = mapper.readTree(jsonInfo);
        System.out.println("JSON ROOT: " + root);


        return root;
    }

    @GetMapping({"/api/bus/", "/api/bus/all", "/api/bus"})
    public ResponseEntity<?> requestAllBuses() {
        try {
            return ResponseEntity.ok(requestBusAPI(url + "&getBuses=1&speed=1"));
        } catch (JsonProcessingException e) {
            System.out.println("Error in /api/bus/all" + e);
            return ResponseEntity.badRequest().body("Error finding buses");
        }

    }

    @GetMapping("/api/bus/id/{busId}")
    public ResponseEntity<?> requestBus(@PathVariable(required = false) String busId) {
        if (busId == null || busId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("busId parameter is missing");
        }

        try {
            return ResponseEntity.ok(requestBusAPI(url + "&bus=1&speed=1&busId=" + busId));
        } catch (JsonProcessingException e) {
            System.out.println("Error in /api/bus/id" + e);
            return ResponseEntity.badRequest().body("Error finding bus");
        }

    }
}
