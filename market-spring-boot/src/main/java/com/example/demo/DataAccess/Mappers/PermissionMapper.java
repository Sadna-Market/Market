package com.example.demo.DataAccess.Mappers;

import com.example.demo.DataAccess.Entity.DataPermission;
import com.example.demo.DataAccess.Enums.PermissionType;
import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.Market.Permission;
import com.example.demo.Domain.Market.permissionType;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PermissionMapper {

    DataServices dataServices;

    HashMap<Triple, Permission> permissions;
    private static class Triple {
        String grantee;
        String grantor;
        int storeId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Triple triple = (Triple) o;
            return storeId == triple.storeId && Objects.equals(grantee, triple.grantee) && Objects.equals(grantor, triple.grantor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(grantee, grantor, storeId);
        }

        public Triple(String grantee, String grantor, int storeId) {
            this.grantee = grantee;
            this.grantor = grantor;
            this.storeId = storeId;
        }
    }


    public void removePermision(String grantee,String grantor ,int StoreID)
    {
        if(permissions.containsKey(new Triple(grantee,grantor,StoreID)))
        {
            permissions.remove(new Triple(grantee,grantor,StoreID));
        }
    }


    private static class PermissionMapperWrapper {
        static PermissionMapper single_instance = new PermissionMapper();
    }
    private PermissionMapper() {
        permissions = new HashMap<>();

    }

    public static PermissionMapper getInstance() {
        return PermissionMapper.PermissionMapperWrapper.single_instance;
    }

    public void  setDataService(DataServices dataServices){
        this.dataServices = dataServices;
    }


    public List<Permission> getStorePermission(Integer storeId)
    {
         List<DataPermission> dataPermissionList =dataServices.getPermissionService().getPermissionStore(storeId);
         List<Permission> storePermissions = new LinkedList<>();
         for(DataPermission dataPermission : dataPermissionList) {
             String grantor = dataPermission.getPermissionId().getGrantorId();
             String grantee= dataPermission.getPermissionId().getGranteeId();
             if (permissions.containsKey(new Triple(grantee,grantor,storeId))){
                 storePermissions.add( permissions.get(new Triple(grantee,grantor,storeId)));
             }
             else {
                 Triple t = new Triple(grantee, grantor, storeId);
                 Permission domainPermission = convertPermissionToDomain(t,dataPermission);
                 storePermissions.add(domainPermission);
             }
         }
         return storePermissions;
    }

    public List<Permission> getGrantorPermissions(String grantor)
    {
        List<DataPermission> dataPermissionList =dataServices.getPermissionService().getAllPermissionsGrantor(grantor);
        List<Permission> storePermissions = new LinkedList<>();
        for(DataPermission dataPermission : dataPermissionList) {
            int storeId =dataPermission.getPermissionId().getStoreId();
            String grantee= dataPermission.getPermissionId().getGranteeId();
            if (permissions.containsKey(new Triple(grantee,grantor,storeId))){
                storePermissions.add( permissions.get(new Triple(grantee,grantor,storeId)));
            }
            else {
                Triple t = new Triple(grantee, grantor, storeId);
                Permission domainPermission = convertPermissionToDomain(t,dataPermission);
                storePermissions.add(domainPermission);
            }
        }
        return storePermissions;
    }


    public List<Permission> getGranteePermissions(String grantee)
    {
        List<DataPermission> dataPermissionList =dataServices.getPermissionService().getAllPermissionsGrantor(grantee);
        List<Permission> storePermissions = new LinkedList<>();
        for(DataPermission dataPermission : dataPermissionList) {
            int storeId =dataPermission.getPermissionId().getStoreId();
            String grantor= dataPermission.getPermissionId().getGrantorId();
            if (permissions.containsKey(new Triple(grantee,grantor,storeId))){
                storePermissions.add( permissions.get(new Triple(grantee,grantor,storeId)));
            }
            else {
                Triple t = new Triple(grantee, grantor, storeId);
                Permission domainPermission = convertPermissionToDomain(t,dataPermission);
                storePermissions.add(domainPermission);
            }
        }
        return storePermissions;
    }

    private Permission convertPermissionToDomain(Triple t, DataPermission dataPermission){
        Permission res = new Permission(null,null,null);

        permissions.put(t,res);
        //insert before

        List<PermissionType> permissionTypes = dataPermission.getGranteePermissionTypes().stream().collect(Collectors.toList());
        List<permissionType.permissionEnum> permissionTypesDomain = permissionTypes.stream().map(PermissionEnum -> permissionType.permissionEnum.valueOf(PermissionEnum.name())).collect(Collectors.toList());

        User tor = UserMapper.getInstance().getUser(t.grantor);
        User tee = UserMapper.getInstance().getUser(t.grantee);
        Store store = StoreMapper.getInstance().getStore(t.storeId);




        permissions.get(t).setGrantee(tee);
        permissions.get(t).setGrantor(tor);
        permissions.get(t).setStore(store);
        permissions.get(t).setGranteePermissionTypes(permissionTypesDomain);

        return res;
    }


}
