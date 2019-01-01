package com.homework.one.responsitory;

import com.homework.one.bean.Book;
import com.homework.one.bean.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findOrderById(Integer id);

    Page<Order> findOrderByUserIdAndAndBalance(String userId, Boolean balance,Pageable pageable);

    Page<Order> findAllByBalance(Boolean balance, Pageable pageable);
}
