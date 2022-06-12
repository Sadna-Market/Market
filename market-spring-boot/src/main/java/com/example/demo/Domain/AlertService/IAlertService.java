package com.example.demo.Domain.AlertService;
import java.util.UUID;

public interface IAlertService {
    void notifyUser(UUID uuid, String msg);
    void notifyUser(String username, String msg);
    void modifyDelayIfExist(String username, UUID uuid);
}
