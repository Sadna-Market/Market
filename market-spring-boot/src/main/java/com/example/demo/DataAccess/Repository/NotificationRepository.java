package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<DataNotification,Integer> {
    List<DataNotification> findAllByUsername(String username);
}
