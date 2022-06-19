package com.example.demo.DataAccess.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataServices {

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private HistoryService historyService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public RuleService getRuleService() {
        return ruleService;
    }

    public void setRuleService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    public ProductTypeService getProductTypeService() {
        return productTypeService;
    }

    public void setProductTypeService(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    public PermissionService getPermissionService() {
        return permissionService;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
}