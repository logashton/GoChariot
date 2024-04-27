package mhl.gochariot.service;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class RequestDTO {
    private Integer id;
    private String route;
    private String pickUp;
    private String dropOff;
    private String status;
    private Timestamp requestTime;
    private Timestamp acceptTime;
    private Integer userId;
    private String userName;
    private Integer driverId;
    private Integer driverIdPGO;
    private String driverFirstName;
    private String driverLastName;

    public RequestDTO(Integer id, String route, String pickUp, String dropOff, String status, Timestamp requestTime, Timestamp acceptTime, Integer userId, String userName, Integer driverId, Integer driverIdPGO, String driverFirstName, String driverLastName) {
        this.id = id;
        this.route = route;
        this.pickUp = pickUp;
        this.dropOff = dropOff;
        this.status = status;
        this.requestTime = requestTime;
        this.acceptTime = acceptTime;
        this.userId = userId;
        this.userName = userName;
        this.driverId = driverId;
        this.driverIdPGO = driverIdPGO;
        this.driverFirstName = driverFirstName;
        this.driverLastName = driverLastName;
    }
}
