package com.example.demo;

import com.example.demo.Domain.StoreModel.BuyRules.*;
import com.example.demo.Domain.StoreModel.DiscountRule.*;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		BuyRule b = new CategoryBuyRule(new CategoryPred(3,10,6,23));
		BuyRule b2 = new UserBuyRule(new UserPred("dor@gmail.com"));
		BuyRule b3 = new ProductBuyRule(new ProductPred(5,10,20,true));
		List<BuyRule> l = new ArrayList<>();
		l.add(b);l.add(b2);l.add(b3);
		BuyRule or = new OrBuyRule(l);

		DiscountRule s = new SimpleStoreDiscountRule(50);
		DiscountRule ss = new SimpleCategoryDiscountRule(20,2);
		DiscountRule ddd = new ConditionCategoryDiscountRule(new CategoryPred(33,18),30);
		DiscountRule dddd = new ConditionProductDiscountRule(new ProductPred(2,33,40),50);
		DiscountRule dd = new ConditionStoreDiscountRule(new ShoppingBagPred(2,33,40),70);
		List<DiscountRule> ll = new ArrayList<>();
		ll.add(ddd);ll.add(dddd);ll.add(dd);
		DiscountRule and = new AndDiscountRule(ll,2,10);
		List<DiscountRule> lll = new ArrayList<>();
		lll.add(ddd);lll.add(dddd);lll.add(dd);lll.add(and);
		DiscountRule or2 = new OrDiscountRule(ll,4,70);
		List<DiscountRule> addl = new ArrayList<>();
		addl.add(s);addl.add(ss);addl.add(or2);
		DiscountRule add = new XorDiscountRule(addl,"Big discount");
		System.out.println(add.getDiscountRule().value);
		add.setID(1);
		System.out.println(add.getDiscountRule().value);
		//SpringApplication.run(DemoApplication.class, args);
	}



}
