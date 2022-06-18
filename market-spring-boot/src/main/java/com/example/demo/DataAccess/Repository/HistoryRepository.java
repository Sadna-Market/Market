package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataHistory;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Entity.DataUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<DataHistory,Integer> {
    List<DataHistory> findAllByUser(DataUser user);
    List<DataHistory> findAllByStore(DataStore store);
}
