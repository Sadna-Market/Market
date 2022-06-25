package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataProductStore;
import com.example.demo.DataAccess.Entity.DataProductType;
import com.example.demo.DataAccess.Entity.DataStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStoreRepository extends JpaRepository<DataProductStore,Integer> {
    List<DataProductStore> findAllByStore(DataStore storeId);
    @Modifying
    @Query("delete from ProductStore ps where ps.productType.productTypeId=?1 and ps.store.storeId=?2")
    void deleteByProductTypeIdStoreId(Integer product_type_id, Integer store_id);

    @Modifying
    @Query("update ProductStore ps set ps.price=?3 where  ps.productType.productTypeId=?1 and ps.store.storeId=?2")
    void updatePrice(Integer product_type_id, Integer store_id,Double price);

    @Modifying
    @Query("update ProductStore ps set ps.quantity=?3 where  ps.productType.productTypeId=?1 and ps.store.storeId=?2")
    void updateQuantity(Integer product_type_id, Integer store_id,Integer price);


}
