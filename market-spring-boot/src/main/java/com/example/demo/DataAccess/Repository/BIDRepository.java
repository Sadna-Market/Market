package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.CompositeKeys.BIDID;
import com.example.demo.DataAccess.Entity.DataBID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BIDRepository extends JpaRepository<DataBID, BIDID> {
}
