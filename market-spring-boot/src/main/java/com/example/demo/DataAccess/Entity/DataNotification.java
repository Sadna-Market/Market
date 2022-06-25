package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "Notification")
@Table(name = "notifications")
public class DataNotification {
    @Id
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_sequence"
    )
    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name = "message_for")
    private String username;

    @Column(name = "message")
    private String message;

    @Column(name = "sent_date")
    private LocalDate sentDate;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }
}
