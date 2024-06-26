package mhl.gochariot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mhl.gochariot.model.Driver;
import mhl.gochariot.model.DriverName;
import mhl.gochariot.repository.DriverRepository;
import mhl.gochariot.service.DriverNameDTO;
import mhl.gochariot.service.DriverNameService;
import mhl.gochariot.service.DriverService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@RestController
public class BusController {
    @Autowired
    DriverNameService driverNameService;

    @Autowired
    DriverService driverService;

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
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

    public JsonNode requestStopsApi() throws JsonProcessingException {
        String endpoint = "https://passiogo.com/mapGetData.php?getStops=2&deviceId=45567185&withOutdated=1&wBounds=1&buildNo=110&showBusInOos=0&lat=36.0665102&lng=-79.436621";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        headers.set("Origin", "https://passiogo.com");
        headers.set("Referer", "https://passiogo.com/");
        headers.setContentType(MediaType.APPLICATION_JSON);


        String jsonData = "{\"s0\":\"2874\",\"sA\":1}";

        HttpEntity<String> entity = new HttpEntity<>(jsonData, headers);

        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);

        String jsonInfo = response.getBody();
        JsonNode root = mapper.readTree(jsonInfo);
        System.out.println("JSON ROOT STOPS: " + root);


        return root;

    }

    // checks for unknown names to add to the DriverName table
    public void driverWatchDog(JsonNode data) {
        List<DriverNameDTO> knownDriverNames = driverNameService.findAllDriverNames();
        Integer driverId = data.get("theBus").get("driverId").asInt();
        String[] firstLastArr = data.get("theBus").get("driver").asText().split(" ");
        boolean exists = false;

        System.out.println("found driver with id: " + driverId);
        for (DriverNameDTO driverNameDTO : knownDriverNames) {
            System.out.println("comparing against known: " + driverNameDTO.getDriverIDPGO());
            if (driverNameDTO.getDriverIDPGO().equals(driverId)) {
                exists = true;
                break;
            }
        }

        if (exists) {
            System.out.println("updating last seen");
            driverNameService.updateLastSeen(
                    driverId,
                    new Timestamp(System.currentTimeMillis())
            );
        } else {
            DriverName driverName = new DriverName();
            driverName.setDriverIdPGO(driverId);
            driverName.setFirstName(firstLastArr[0]);
            driverName.setLastName(firstLastArr[1]);
            driverName.setFirstSeen(new Timestamp(System.currentTimeMillis()));
            driverName.setLastSeen(new Timestamp(System.currentTimeMillis()));

            driverNameService.saveDriverName(driverName);

        }

    }

    @GetMapping({"/api/bus/", "/api/bus/all", "/api/bus"})
    public ResponseEntity<?> requestAllBuses() {
        try {
            JsonNode data = requestBusAPI(url + "&getBuses=1&speed=1");
            return ResponseEntity.ok(data);
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
            JsonNode data = requestBusAPI(url + "&bus=1&speed=1&busId=" + busId);
            driverWatchDog(data);
            return ResponseEntity.ok(data);
        } catch (JsonProcessingException e) {
            System.out.println("Error in /api/bus/id" + e);
            return ResponseEntity.badRequest().body("Error finding bus");
        }

    }

    @GetMapping("/api/bus/stops")
    public ResponseEntity<?> requestBusStops() {
        try {
            JsonNode data = requestStopsApi();
            return ResponseEntity.ok(data);
        } catch (JsonProcessingException e) {
            System.out.println("Erorr in /api/bus/stops" + e);
            return ResponseEntity.badRequest().body("Error finding stops");
        }
    }

    @GetMapping("/api/bus/verified/id/{driverIdPGO}")
    public ResponseEntity<?> isDriverVerified(@PathVariable() Integer driverIdPGO) {
        boolean driverFound = driverService.doesExistByDriverIdPGO(driverIdPGO);

        return ResponseEntity.ok(driverFound);


    }
}
