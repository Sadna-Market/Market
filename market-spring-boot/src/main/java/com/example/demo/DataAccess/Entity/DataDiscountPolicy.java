package com.example.demo.DataAccess.Entity;

import javax.persistence.*;


@Entity(name = "DiscountPolicy")
@Table(name = "discount_policy")
public class DataDiscountPolicy {

    @Id
    @SequenceGenerator(
            name = "discount_policy_sequence",
            sequenceName = "discount_policy_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "discount_policy_sequence"
    )
    @Column(
            name = "discount_policy_id"
    )
    private Integer discountPolicyId;

    //TODO: add rules field

    public DataDiscountPolicy() {
    }
}