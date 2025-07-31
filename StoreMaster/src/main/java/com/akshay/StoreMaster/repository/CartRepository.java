package com.akshay.StoreMaster.repository;

import com.akshay.StoreMaster.entity.Cart;
import com.akshay.StoreMaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(User user);
}
