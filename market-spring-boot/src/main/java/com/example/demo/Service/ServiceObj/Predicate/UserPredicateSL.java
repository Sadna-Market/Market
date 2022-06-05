package com.example.demo.Service.ServiceObj.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Domain.UserModel.User;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class UserPredicateSL implements PredicateSL {
    public String username;  //can't buy in this store

    public UserPredicateSL(String username){
        this.username = username;
    }


    @Override
    public String getPredicateBuyRule() {
        return "user email: " + username + " can't buy";
    }

    @Override
    public String getPredicateDiscountRule() {
        return null;
    }

    @Override
    public SLResponseOBJ<Predicate> convertToPredicateDL() {
        if(username == null || username.isEmpty())
            return new SLResponseOBJ<>(null,ErrorCode.INVALID_ARGS_FOR_RULE);
        return new SLResponseOBJ<>(new UserPred(username));
    }
}
