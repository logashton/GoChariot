package mhl.gochariot.service;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class DriverNameDTO {
    private Integer driverIDPGO;
    private String firstName;
    private String lastName;
    private Integer driverIdPGO;
    private Integer userId;
    private Timestamp firstSeen;
    private Timestamp lastSeen;

    public DriverNameDTO(Integer driverIdPGO, String firstName, String lastName, Integer userId, Timestamp firstSeen,  Timestamp lastSeen) {
        this.driverIDPGO = driverIdPGO;
        this.firstName = firstName;
        this.lastName = lastName;
        this.driverIdPGO = driverIDPGO;
        this.userId = userId;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
    }

    @Override
    public String toString() {
        return "DriverNameDTO{" +
                "driverIDPGO=" + driverIDPGO +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userId=" + userId +
                ", firstSeen=" + firstSeen +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
