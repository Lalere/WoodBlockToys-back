package com.zenika.academy.WoodBlockToys.order;

import com.zenika.academy.WoodBlockToys.account.Account;
import com.zenika.academy.WoodBlockToys.barrel.Barrel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double price;

    @Enumerated(EnumType.STRING)
    //Default value = "WAITING_FOR_VALIDATION"
    private OrderStatus status = OrderStatus.WAITING_FOR_VALIDATION;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private Barrel barrel;

}
