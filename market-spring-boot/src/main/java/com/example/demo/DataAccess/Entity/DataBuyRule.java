package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "BuyRule")
@Table(name = "buy_rule")
public class DataBuyRule {

    @Id
    @SequenceGenerator(
            name = "buy_rule_sequence",
            sequenceName = "buy_rule_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "buy_rule_sequence"
    )
    @Column(name = "buy_rule_id")
    private Integer buyRuleId;

    @Column(name = "rule")
    private String rule;

    @ManyToOne
    @JoinColumn(name = "store",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(
                    name = "store_fk"
            ))
    private DataStore store;


    public DataBuyRule() {
    }


    public Integer getBuyRuleId() {
        return buyRuleId;
    }

    public void setBuyRuleId(Integer buyRuleId) {
        this.buyRuleId = buyRuleId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }
}
