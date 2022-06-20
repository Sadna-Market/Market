package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataProductStoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStoreHistoryRepository extends JpaRepository<DataProductStoreHistory,Integer> {
}
