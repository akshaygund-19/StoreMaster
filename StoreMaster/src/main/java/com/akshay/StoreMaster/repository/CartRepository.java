package com.akshay.StoreMaster.repository;

import com.akshay.StoreMaster.entity.Cart;
import com.akshay.StoreMaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    Optional<Cart> findByUserId(Long userId);
}
