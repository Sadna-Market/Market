package com.example.demo.DataAccess.Repository;

import com.example.demo.DataAccess.CompositeKeys.PermissionId;
import com.example.demo.DataAccess.Entity.DataPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<DataPermission, PermissionId> {
}
