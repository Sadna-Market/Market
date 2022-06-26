package com.example.demo.DataAccess.Mappers;

import com.example.demo.DataAccess.Entity.DataProductType;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Services.*;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.Store;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProductTypeMapper {
    HashMap<Integer, ProductType> productTypeHashMap;
     DataServices dataServices;


    private static class ProductTypeMapperWrapper {
        static ProductTypeMapper single_instance = new ProductTypeMapper();
    }
    private ProductTypeMapper() {
        this.productTypeHashMap = new HashMap<>();
    }

    public static ProductTypeMapper getInstance() {
        return ProductTypeMapper.ProductTypeMapperWrapper.single_instance;
    }

    public void  setDataService(DataServices dataServices){
        this.dataServices = dataServices;
    }

    public ProductType getProductType(Integer pid)
    {
        if(productTypeHashMap.containsKey(pid))
        {
            return productTypeHashMap.get(pid);
        }
        DataProductType dataProductType = dataServices.getProductTypeService().getProductTypeById(pid);
        if(dataProductType == null){
            return null;
        }
        ProductType productType = convertToDomainProductType(dataProductType);
        productTypeHashMap.put(pid,productType);
        return productType;
    }


    public List<ProductType> getAllProductTypes()
    {
        List<DataProductType> dataProductTypeList = dataServices.getProductTypeService().getAllProductTypes();
        List<ProductType> res =new LinkedList<>();
        for(DataProductType dataProductType : dataProductTypeList){
            ProductType productType = convertToDomainProductType(dataProductType);
            res.add(productType);
            productTypeHashMap.put(productType.getProductID().value,productType);
        }
        return res;
    }

    private ProductType convertToDomainProductType(DataProductType dataProductType){
        Set<Integer> dataStoreList =dataProductType.getStores();
        return new ProductType(dataProductType.getProductTypeId(),dataProductType.getRate(),dataProductType.getCounter_rates(),
                dataProductType.getProductName(),dataProductType.getDescription(),
                dataProductType.getCategory(),dataStoreList.stream().collect(Collectors.toList()));
    }

}
