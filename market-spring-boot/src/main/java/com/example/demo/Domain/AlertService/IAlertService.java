package com.example.demo.Domain.AlertService;
import java.util.List;
import java.util.UUID;

public interface IAlertService {
    void notifyUser(UUID uuid, String msg,String username);
    void notifyUsers(List<Notification> toPersist);
    void modifyDelayIfExist(String username, UUID uuid);
}
