package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;
import com.example.demo.DataAccess.Entity.DataShoppingBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingBagRepository extends JpaRepository<DataShoppingBag, ShoppingBagId> {
    List<DataShoppingBag> findAllByShoppingBagId_Username(String username);
}
