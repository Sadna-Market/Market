package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataProductType;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductTypeServiceTest {
    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private Facade market;

    @Test
    @Transactional
    void insertProductType() {
        ProductType productType = new ProductType("myProduct","blbabla",3);
        DataProductType dataProductType = productType.getDataObject();
        //action
        assertTrue(productTypeService.insertProductType(dataProductType));
        //check

        assertNotEquals(0, dataProductType.getProductTypeId());
    }

    @Test
    @Transactional
    void deleteProductType() {
        ProductType productType = new ProductType("myProduct","blbabla",3);
        DataProductType dataProductType = productType.getDataObject();
        assertTrue(productTypeService.insertProductType(dataProductType));
        assertNotEquals(0,dataProductType.getProductTypeId());
        //action
        assertTrue(productTypeService.deleteProductType(dataProductType.getProductTypeId()));
        //check
        DataProductType nullStore = productTypeService.getProductTypeById(dataProductType.getProductTypeId());
        assertNull(nullStore);
    }

    @Test
    @Transactional
    void updateProductType() {
        //pre
        ProductType productType = new ProductType("myProduct","blbabla",3);
        DataProductType dataProductType = productType.getDataObject();
        assertTrue(productTypeService.insertProductType(dataProductType));
        //action
        dataProductType.setProductName("mojo");
        dataProductType.setCategory(5);
        dataProductType.setRate(100);
        assertTrue(productTypeService.updateProductType(dataProductType));

        //check
        List<DataProductType> afterProductTypes = productTypeService.getAllProductTypes();
        assertEquals(1, afterProductTypes.size());
        DataProductType afterProductType = afterProductTypes.get(0);
        assertEquals(100, afterProductType.getRate());
        assertEquals(5, afterProductType.getCategory());
        assertEquals("mojo", afterProductType.getProductName());
    }

    @Test
    @Transactional
    void getProductTypeById() {
        ProductType productType = new ProductType("myProduct","blbabla",3);
        DataProductType dataProductType = productType.getDataObject();
        assertTrue(productTypeService.insertProductType(dataProductType));
        //action
        DataProductType afterProductType = productTypeService.getProductTypeById(dataProductType.getProductTypeId());
        //check
        assertEquals(dataProductType.getRate(), afterProductType.getRate());
        assertEquals(dataProductType.getCategory(), afterProductType.getCategory());
        assertEquals(dataProductType.getProductName(), afterProductType.getProductName());
    }

    @Test
    @Transactional
    void getAllProductTypes() {
        ProductType productType1 = new ProductType("myProduct1","blbabla1",3);
        ProductType productType2 = new ProductType("myProduct2","blbabla2",4);
        ProductType productType3 = new ProductType("myProduct3","blbabla3",5);
        ProductType productType4 = new ProductType("myProduct4","blbabla4",6);
        ProductType productType5 = new ProductType("myProduct5","blbabla5",7);
        List<DataProductType> dataProductTypes = List.of(
                productType1.getDataObject(),
                productType2.getDataObject(),
                productType3.getDataObject(),
                productType4.getDataObject(),
                productType5.getDataObject());
        dataProductTypes.forEach(dataProductType -> {
            assertTrue(productTypeService.insertProductType(dataProductType));
        });
        //action
        List<DataProductType> afterProductTypes = productTypeService.getAllProductTypes();
        //check
        assertEquals(5, afterProductTypes.size());
        for (int i = 0; i < 5; i++) {
            DataProductType dataProductType = dataProductTypes.get(i);
            DataProductType afterProductType = afterProductTypes.get(i);
            assertEquals(dataProductType.getRate(), afterProductType.getRate());
            assertEquals(dataProductType.getCategory(), afterProductType.getCategory());
            assertEquals(dataProductType.getProductName(), afterProductType.getProductName());
        }
    }

    @Test
    @Transactional
    void addNewProductType(){
        String uuid = market.guestVisit().value;
        SLResponseOBJ<String> result = market.login(uuid, "sysManager@gmail.com", "Shalom123$");
        assertFalse(result.errorOccurred());
        uuid = result.value;
        var res = market.addNewProductType(uuid,"newProduct","hello",1);
        assertFalse(res.errorOccurred());
        var p = productTypeService.getAllProductTypes();
        assertFalse(p.isEmpty());
        assertEquals(p.get(0).getProductTypeId(), res.value);
    }
}