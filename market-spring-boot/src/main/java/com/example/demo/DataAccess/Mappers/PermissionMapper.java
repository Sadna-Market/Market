package com.example.demo.DataAccess.Mappers;

import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.Market.Permission;

import java.util.HashMap;
import java.util.Objects;

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
//    public Permission getStorePermmision(Integer storeId)
//    {
//
//
//    }


}
