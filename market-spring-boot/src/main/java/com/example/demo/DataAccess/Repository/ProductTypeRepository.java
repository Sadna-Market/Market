package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<DataProductType,Integer> {
    @Modifying
    @Query("update ProductType p set p.rate=?2, p.counter_rates=?3 where p.productTypeId=?1")
    void updateProductRate(Integer productId ,Integer rate,Integer counterRate);
}
