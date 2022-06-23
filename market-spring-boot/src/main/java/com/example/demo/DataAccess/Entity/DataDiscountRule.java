package com.example.demo.DataAccess.Entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity(name = "DiscountRules")
@Table(name = "discount_rules")
public class DataDiscountRule {

    @Id
    @SequenceGenerator(
            name = "discount_rule_sequence",
            sequenceName = "discount_rule_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "discount_rule_sequence"
    )
    @Column(
            name = "discount_rule_id"
    )
    private Integer discountRuleId;

    @Column(name = "rule",columnDefinition="TEXT")
    private String rule;

    @ManyToOne
    @JoinColumn(name = "store",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(
                    name = "store_fk"
            ))
    private DataStore store;


    public DataDiscountRule() {
    }

    public Integer getDiscountRuleId() {
        return discountRuleId;
    }

    public void setDiscountRuleId(Integer discountRuleId) {
        this.discountRuleId = discountRuleId;
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
