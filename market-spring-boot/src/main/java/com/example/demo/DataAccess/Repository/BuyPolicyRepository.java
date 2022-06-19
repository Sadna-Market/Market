package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataBuyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyPolicyRepository extends JpaRepository<DataBuyPolicy,Integer> {
}
