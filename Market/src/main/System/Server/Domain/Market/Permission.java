package main.System.Server.Domain.Market;

import main.System.Server.Domain.UserComponent.User;

public class Permission {
    User grantee;
    Store store;
    User grantor;

    Permission(User grantee, Store store, User grantor){
        // grantee.accessPers ^ store.accessPers = nothing;
        // grantee != grantor;
        // grantor has permission;
    }
}