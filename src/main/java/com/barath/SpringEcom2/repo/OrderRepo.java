package com.barath.SpringEcom2.repo;

import com.barath.SpringEcom2.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    Optional<Order> findByOrderId(String orderId);
}
