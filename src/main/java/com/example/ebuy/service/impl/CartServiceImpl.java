package com.example.ebuy.service.impl;
import com.example.ebuy.model.Product;
import com.example.ebuy.repository.ProductRepository;
import com.example.ebuy.service.CartService;
import com.example.ebuy.utlis.ShoppingCart;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@SessionScope
@Transactional
public class CartServiceImpl implements CartService {
    private final ShoppingCart shoppingCart;
    private final ProductRepository productRepository;
    private final OrderDetailsServiceImpl orderDetailsServiceImpl;
    private final ProductServiceImpl productServiceImpl;

    public CartServiceImpl(ProductRepository productRepository, OrderDetailsServiceImpl orderDetailsServiceImpl, ProductServiceImpl productServiceImpl) {
        this.productRepository = productRepository;
        this.orderDetailsServiceImpl = orderDetailsServiceImpl;
        this.productServiceImpl = productServiceImpl;
        shoppingCart = new ShoppingCart();
    }


    public void addProduct(long id) {
        if(productServiceImpl.get(id).isPresent()) {
            Product stockProduct = productServiceImpl.get(id).get();
            shoppingCart.addProduct(stockProduct);
        }
    }

    public boolean removeProduct(long id) {
        if(productServiceImpl.get(id).isPresent()) {
            Product stockProduct = productServiceImpl.get(id).get();
            shoppingCart.removeProduct(stockProduct);
            return true;
        }
        return false;
    }

    public ShoppingCart getCart() {
        return shoppingCart;
    }


    public long checkout() {
        long id = -1;
        List<Long> toRemove = new ArrayList<>();
        AtomicBoolean checkedOut = new AtomicBoolean(true);
        // updating stock with the database to check against cart
        if(shoppingCart.isEmpty()) {
            return id;
        }

        shoppingCart.getProducts().values().forEach(product -> {
            if(productServiceImpl.get(product.getId()).isPresent()) {
                if(productServiceImpl.get(product.getId()).get().getQuantity() < product.getQuantity()) {
                    checkedOut.set(false);
                    // remove product from cart and put back to sessionStock
                    toRemove.add(product.getId());
                    // update session stock
                }
            } else {
                checkedOut.set(false);
                toRemove.add(product.getId());
            }
        });
        // to avoid ConcurrentModificationException,
        // remove the products from shopping cart after iterating over it
        toRemove.forEach(productId -> shoppingCart.getProducts().remove(productId));
        if(checkedOut.get()) {
            shoppingCart.getProducts().values()
                .forEach(product ->
                    productServiceImpl.get(product.getId())
                        .ifPresent(product1 -> {
                            product1.setQuantity(product1.getQuantity() - product.getQuantity());
                            product1.setArchived(true);
                            productRepository.save(product1);
                        }));
            id = orderDetailsServiceImpl.save(new ArrayList<>(shoppingCart.getProducts().values()));
            shoppingCart.getProducts().clear();
            shoppingCart.setTotal(BigDecimal.ZERO);
        }
        return id;
    }


}
