package mhl.gochariot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import jakarta.persistence.*;
import mhl.gochariot.model.Driver;
import mhl.gochariot.model.User;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "Request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "Route", nullable = false)
    private String route;

    @NonNull
    @Column(name = "PickUp", nullable = false)
    private String pickUp;

    @NonNull
    @Column(name = "DropOff", nullable = false)
    private String dropOff;

    @Column(name = "Status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DriverId")
    private Driver driver;

    @NonNull
    @Column(name = "RequestTime", nullable = false)
    private Timestamp requestTime;

    @Column(name = "AcceptTime")
    private Timestamp acceptTime;

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", route='" + route + '\'' +
                ", pickUp='" + pickUp + '\'' +
                ", dropOff='" + dropOff + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", driver=" + driver +
                ", requestTime=" + requestTime +
                ", acceptTime=" + acceptTime +
                '}';
    }
}
