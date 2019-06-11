package com.zenika.academy.WoodBlockToys.order;

import com.zenika.academy.WoodBlockToys.account.Account;
import com.zenika.academy.WoodBlockToys.account.AccountRepository;
import com.zenika.academy.WoodBlockToys.barrel.Barrel;
import com.zenika.academy.WoodBlockToys.barrel.BarrelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private BarrelRepository barrelRepository;
    private AccountRepository accountRepository;

    public OrderService(OrderRepository orderRepository, BarrelRepository barrelRepository, AccountRepository accountRepository) {
        this.orderRepository = orderRepository;
        this.barrelRepository = barrelRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(noRollbackFor = {EntityExistsException.class})
    Order saveOrder(Order order) {
        Optional<Order> currentOrder = orderRepository
                .findByBarrelIdAndAccountEmail(order.getBarrel().getId(), order.getAccount().getEmail());
        Optional<Account> currentAccount = accountRepository.findByEmail(order.getAccount().getEmail());
        Optional<Barrel> currentBarrel = barrelRepository.findById(order.getBarrel().getId());
        if (currentOrder.isPresent()) {
            throw new EntityExistsException("Order already exists");
        } else if (currentAccount.isEmpty() || currentBarrel.isEmpty()) {
            throw new EntityNotFoundException("Account or barrel cannot be found");
        } else {
            order.setPrice(currentBarrel.get().getPrice());
            order.setAccount(currentAccount.get());
            return orderRepository.save(order);
        }
    }


    @Transactional(noRollbackFor = {EntityNotFoundException.class})
    Order updateOrder(Order order, Long id) {
        order.setId(id);
        Optional<Order> currentOrder = orderRepository.findById(id);

        if (currentOrder.isEmpty()) {
            throw new EntityNotFoundException("Order not found");
        } else {
            return orderRepository.save(order);
        }
    }

    void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    Order getOrder(Long id) {
        Optional<Order> currentOrder = orderRepository.findById(id);

        if (currentOrder.isEmpty()) {
            throw new EntityNotFoundException("Order not found");
        } else {
            return currentOrder.get();
        }
    }

    List<Order> getOrderList() {
        List<Order> orderList = new ArrayList<>();
        orderRepository.findAll().forEach(orderList::add);
        return orderList;
    }
}
