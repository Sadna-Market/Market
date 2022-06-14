package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<DataHistory,Integer> {
}
