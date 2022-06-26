package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;
import com.example.demo.DataAccess.Entity.DataShoppingBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingBagRepository extends JpaRepository<DataShoppingBag, ShoppingBagId> {
    List<DataShoppingBag> findAllByShoppingBagId_Username(String username);

    @Modifying
    @Query("delete from ShoppingBags sb where sb.shoppingBagId.username=?1")
    void deleteAllUsernameBags(String username);
}
