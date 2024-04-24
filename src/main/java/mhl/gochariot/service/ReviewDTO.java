package mhl.gochariot.service;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;


@Getter
@Setter
public class ReviewDTO {
    private Integer id;
    private String userFirstName;
    private String userLastName;
    private String driverFirstName;
    private String driverLastName;
    private Integer driverIdPGO;
    private String username;
    private Double rating;
    private String content;
    private Timestamp createdAt;

    public ReviewDTO(Integer id, String firstName, String lastName, String driverFirstName, String driverLastName, Integer driverIdPGO, String username, Double rating, String content, Timestamp createdAt) {
        this.id = id;
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.driverFirstName = driverFirstName;
        this.driverLastName = driverLastName;
        this.driverIdPGO = driverIdPGO;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }
}