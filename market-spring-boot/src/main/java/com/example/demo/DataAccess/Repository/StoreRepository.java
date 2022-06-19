package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Entity.DataUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StoreRepository extends JpaRepository<DataStore,Integer> {
    /*
    * if you want to add any particular query method just add.
    * for example: I want a query to find all stores by 'founder':
    * add here : List<DataStore> findAllByFounder(String founder);
    * Note: 'founder field is a  String in DataStore Entity, therefore JPA knows how map it.
    * */
}
