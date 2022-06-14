package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DataUser,String> {
}
