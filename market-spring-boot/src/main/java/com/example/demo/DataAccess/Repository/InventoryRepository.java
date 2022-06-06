package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<DataInventory,Integer> {
}
