package mhl.gochariot.service;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;


@Getter
@Setter
public class ReviewDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private Double rating;
    private String content;
    private Timestamp createdAt;

    public ReviewDTO(Integer id, String firstName, String lastName, String username, Double rating, String content, Timestamp createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }
}