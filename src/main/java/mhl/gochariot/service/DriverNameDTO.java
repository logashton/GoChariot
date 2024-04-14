package mhl.gochariot.service;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class DriverNameDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer userId;
    private Timestamp firstSeen;
    private Timestamp lastSeen;

    public DriverNameDTO(Integer id, String firstName, String lastName, Integer userId, Timestamp firstSeen,  Timestamp lastSeen) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
    }
}
