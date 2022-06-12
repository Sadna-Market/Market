package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataDiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountPolicyRepository extends JpaRepository<DataDiscountPolicy,Integer> {
}
