package com.zenika.academy.WoodBlockToys.order;

import com.zenika.academy.WoodBlockToys.account.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Account> findByAccountEmail(String email);

    Optional<Order> findByBarrelIdAndAccountEmail(Long id, String email);
}
