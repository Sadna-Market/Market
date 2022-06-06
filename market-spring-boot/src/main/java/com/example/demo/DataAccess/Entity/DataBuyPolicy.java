package com.example.demo.DataAccess.Entity;

import javax.persistence.*;

@Entity(name = "BuyPolicy")
@Table(name = "buy_policy")
public class DataBuyPolicy {

    @Id
    @SequenceGenerator(
            name = "buy_policy_sequence",
            sequenceName = "buy_policy_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "buy_policy_sequence"
    )
    @Column(name = "buy_policy_id")
    private Integer buyPolicyID;

    //TODO: add rules field


    public DataBuyPolicy() {
    }

    public Integer getBuyPolicyID() {
        return buyPolicyID;
    }

    public void setBuyPolicyID(Integer buyPolicyID) {
        this.buyPolicyID = buyPolicyID;
    }
}
