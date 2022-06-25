package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.CompositeKeys.PermissionId;
import com.example.demo.DataAccess.Entity.DataPermission;
import com.example.demo.Domain.Market.Permission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<DataPermission, PermissionId> {
    List<DataPermission> findAllByPermissionId_GranteeId(String grantee);
    List<DataPermission> findAllByPermissionId_GrantorId(String grantor);
    List<DataPermission> findAllByPermissionId_StoreId(Integer store);
    List<DataPermission> findAllByPermissionId_GrantorIdAndPermissionId_GranteeIdAndPermissionId_StoreId(String grantor,String grantee,Integer store);
}
