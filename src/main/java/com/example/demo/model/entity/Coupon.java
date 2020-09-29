package com.example.demo.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "sc_coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "min_cart_amount")
    private Double minCartAmount;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "discount_amount")
    private Double discountAmount;
}
