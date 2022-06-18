package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataDiscountRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRuleRepository extends JpaRepository<DataDiscountRule,Integer> {
}
