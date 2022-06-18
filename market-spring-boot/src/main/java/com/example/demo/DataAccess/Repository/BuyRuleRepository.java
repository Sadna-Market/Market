package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyRuleRepository extends JpaRepository<DataBuyRule,Integer> {
}
