package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.CompositeKeys.PermissionId;
import com.example.demo.DataAccess.Entity.DataPermission;
import com.example.demo.DataAccess.Entity.DataProductType;
import com.example.demo.DataAccess.Enums.PermissionType;
import com.example.demo.DataAccess.Repository.PermissionRepository;
import com.example.demo.DataAccess.Repository.ProductTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private static final Logger logger = Logger.getLogger(PermissionService.class);

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository productTypeRepository) {
        this.permissionRepository = productTypeRepository;
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public boolean insertPermission(DataPermission permission) {
        try {
            DataPermission dataPermission = permissionRepository.saveAndFlush(permission);
            logger.info(String.format("inserted permission (grantee,grantor,storeId) ~ (%s,%s,%d) successfully to db",
                    dataPermission.getPermissionId().getGranteeId(),
                    dataPermission.getPermissionId().getGrantorId(),
                    dataPermission.getPermissionId().getStoreId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to insert permission (grantee,grantor,storeId) ~ (%s,%s,%d) into db, ERROR: %s",
                    permission.getPermissionId().getGranteeId(),
                    permission.getPermissionId().getGrantorId(),
                    permission.getPermissionId().getStoreId(),
                    e.getMessage()));
            return false;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean updatePermissionType(DataPermission permission) {
        var permissionId = permission.getPermissionId();
        try {
            var optionalDataPermission = permissionRepository.findById(permissionId);
            if (optionalDataPermission.isEmpty()) {
                logger.warn(String.format("permission (grantee,grantor,storeId) ~ (%s,%s,%d) not found in db",
                        permissionId.getGranteeId(),
                        permissionId.getGrantorId(),
                        permissionId.getStoreId()));
                return false;
            }
            optionalDataPermission.get().update(permission);
            permissionRepository.save(optionalDataPermission.get());
            logger.info(String.format("updated permission (grantee,grantor,storeId) ~ (%s,%s,%d) successfully from db",
                    permissionId.getGranteeId(),
                    permissionId.getGrantorId(),
                    permissionId.getStoreId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to update permission (grantee,grantor,storeId) ~ (%s,%s,%d) from db, ERROR: %s",
                    permissionId.getGranteeId(),
                    permissionId.getGrantorId(),
                    permissionId.getStoreId(),
                    e.getMessage()));
            return false;
        }
    }


    @Transactional(rollbackFor = {Exception.class})
    public boolean deletePermission(PermissionId permissionId) {
        try {
            permissionRepository.deleteById(permissionId);
            logger.info(String.format("deleted permission (grantee,grantor,storeId) ~ (%s,%s,%d) successfully from db",
                    permissionId.getGranteeId(),
                    permissionId.getGrantorId(),
                    permissionId.getStoreId()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("failed to delete permission (grantee,grantor,storeId) ~ (%s,%s,%d) from db, ERROR: %s",
                    permissionId.getGranteeId(),
                    permissionId.getGrantorId(),
                    permissionId.getStoreId(),
                    e.getMessage()));
            return false;
        }
    }



    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public DataPermission getPermissionById(PermissionId permissionId) {
        try {
            Optional<DataPermission> dataPermission = permissionRepository.findById(permissionId);
            if (dataPermission.isEmpty()) {
                logger.warn(String.format("permission (grantee,grantor,storeId) ~ (%s,%s,%d) is not present in db",
                        permissionId.getGranteeId(),
                        permissionId.getGrantorId(),
                        permissionId.getStoreId()));
                return null;
            }
            logger.info(String.format("fetched permission (grantee,grantor,storeId) ~ (%s,%s,%d) successfully from db",
                    dataPermission.get().getPermissionId().getGranteeId(),
                    dataPermission.get().getPermissionId().getGrantorId(),
                    dataPermission.get().getPermissionId().getStoreId()));
            return dataPermission.get();
        } catch (Exception e) {
            logger.error(String.format("failed to fetch permission (grantee,grantor,storeId) ~ (%s,%s,%d) from db, ERROR: %s",
                    permissionId.getGranteeId(),
                    permissionId.getGrantorId(),
                    permissionId.getStoreId(),
                    e.getMessage()));
            return null;
        }
    }

    @Transactional(rollbackFor = {Exception.class}, timeout = 10)
    public List<DataPermission> getAllPermissions() {
        try {
            List<DataPermission> dataPermissionList = permissionRepository.findAll();
            logger.info("fetched all permissions successfully from db");
            return dataPermissionList;
        } catch (Exception e) {
            logger.error(String.format("failed to fetch all permissions from db, ERROR: %s", e.getMessage()));
            return null;
        }
    }

}
