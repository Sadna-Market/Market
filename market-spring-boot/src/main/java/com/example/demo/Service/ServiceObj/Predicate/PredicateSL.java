package com.example.demo.Service.ServiceObj.Predicate;

import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public interface PredicateSL {
    String getPredicateBuyRule();
    String getPredicateDiscountRule();
    SLResponseOBJ<Predicate> convertToPredicateDL();
}
