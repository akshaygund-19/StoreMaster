package com.akshay.StoreMaster.service;

import com.akshay.StoreMaster.dto.AddCartDTO;
import com.akshay.StoreMaster.dto.CartItemDTO;
import com.akshay.StoreMaster.dto.CartResponseDTO;
import com.akshay.StoreMaster.entity.Cart;
import com.akshay.StoreMaster.entity.CartItem;
import com.akshay.StoreMaster.entity.Product;
import com.akshay.StoreMaster.entity.User;
import com.akshay.StoreMaster.repository.CartItemRepositoty;
import com.akshay.StoreMaster.repository.CartRepository;
import com.akshay.StoreMaster.repository.ProductRepository;
import com.akshay.StoreMaster.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepositoty cartItemRepositoty;

    @Transactional
    public void addCart(AddCartDTO addCartDTO) {
        log.info("Received addCart request: userId: {}, productId: {}, quantity: {}", addCartDTO.getUserId(), addCartDTO.getProductId(), addCartDTO.getQuantity());

        Product product = productRepository.findById(addCartDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found with ID: " + addCartDTO.getProductId()));
        log.info("Fetched Product: id: {}, name: {}, price: {}", product.getProduct_Id(), product.getName(), product.getPrice());

        User user = userRepository.findById(addCartDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found with ID " + addCartDTO.getUserId()));
        log.info("Fetched User: id:{}", user.getId());

        // Check if the user already has a cart
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            log.info("No existing cart found for userId : {}. Creating new cart.", user.getId());
            cart = new Cart();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cart = cartRepository.save(cart);
            log.info("New cart created: cartId: {}", cart.getId());
        } else {
            log.info("Existing cart found: cartId: {}", cart.getId());
        }

        Optional<CartItem> existingItem = cart.getCartItemList().stream().filter(item -> item.getProduct().getProduct_Id().equals(addCartDTO.getProductId())).findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            log.info("Product already in cart. Updating quantity. Old quantity: {}, adding: {}", item.getQuantity(), addCartDTO.getQuantity());
            item.setQuantity(item.getQuantity() + addCartDTO.getQuantity());
            item.setPrice(item.getQuantity() * product.getPrice());
            log.info("Updated CartItem: productId: {}, quantity: {}, price: {}", item.getProduct().getProduct_Id(), item.getQuantity(), item.getPrice());
            cartItemRepositoty.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(addCartDTO.getQuantity());
            log.info("Quantity: {}, Prize: {}", addCartDTO.getQuantity(), product.getPrice());
            newItem.setPrice(addCartDTO.getQuantity() * product.getPrice());
            log.info("item prize: {}", newItem.getPrice());
            newItem.setCart(cart);
            cart.getCartItemList().add(newItem);
            log.info("Added new CartItem: productId:{}, quantity:{}, price:{}", product.getProduct_Id(), addCartDTO.getQuantity(), newItem.getPrice());
        }
        for (CartItem item : cart.getCartItemList()) {
            log.info("CartItem - Product ID: {}, Name: {}, Quantity: {}, Price: {}", item.getProduct().getProduct_Id(), item.getProduct().getName(), item.getQuantity(), item.getPrice());
        }

        double total = cart.getCartItemList().stream().mapToDouble(CartItem::getPrice).sum();

        cart.setTotalPrice(total);
        cartRepository.save(cart);
        log.info("Total Prize: {}", cart.getTotalPrice());
        log.info("Cart updated: cartId:{}, totalPrice:{}", cart.getId(), cart.getTotalPrice());
    }

    @Transactional
    public CartResponseDTO viewCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found for user with ID: " + userId));

//        for (CartItem item : cart.getCartItemList()) {
//            log.info("CartItem - Product ID: {}, Name: {}, Quantity: {}, Price: {}",
//                    item.getProduct().getProduct_Id(),
//                    item.getProduct().getName(),
//                    item.getQuantity(),
//                    item.getPrice());
//        }

        List<CartItemDTO> cartItems = cart.getCartItemList().stream().map(item -> new CartItemDTO(item.getProduct().getProduct_Id(), item.getProduct().getName(), item.getQuantity(), item.getProduct().getPrice())).collect(Collectors.toList());

//        for (CartItemDTO item: cartItems) {
//            log.info("CartItem - Product ID: {}, Name: {}, Quantity: {}, Price: {}",
//                    item.getProductId(),
//                    item.getProductName(),
//                    item.getQuantity(),
//                    item.getPrice());
//        }
        return new CartResponseDTO(cartItems, cart.getTotalPrice());
    }
}
