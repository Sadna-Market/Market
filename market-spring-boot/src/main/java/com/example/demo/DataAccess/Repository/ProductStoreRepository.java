package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataProductStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStoreRepository extends JpaRepository<DataProductStore,Integer> {
}
