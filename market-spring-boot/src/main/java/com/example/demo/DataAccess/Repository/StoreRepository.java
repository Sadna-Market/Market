package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Entity.DataUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StoreRepository extends JpaRepository<DataStore,Integer> {
    @Modifying
    @Query("update Store s set s.isOpen=?2 where s.storeId=?1")
    void updatedIsOpen(Integer storeId ,Boolean isOpen);

    @Modifying
    @Query("update Store s set s.rate=?2, s.numOfRated=?3 where s.storeId=?1")
    void updateStoreRate(Integer storeId ,Integer rate, Integer numOfRated);
}
