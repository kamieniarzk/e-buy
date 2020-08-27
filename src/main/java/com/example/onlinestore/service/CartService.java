package com.example.onlinestore.service;
import com.example.onlinestore.model.OrderDetails;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@SessionScope
@Transactional
public class CartService {
    private final Map<Long, Product> cart = new LinkedHashMap<>();
    private final ProductRepository productRepository;
    private final OrderDetailsService orderDetailsService;
    private Map<Long, Product> sessionStock = new LinkedHashMap<>();

    public CartService(ProductRepository productRepository, OrderDetailsService orderDetailsService) {
        this.productRepository = productRepository;
        productRepository.findAll()
                .forEach(product -> sessionStock.put(product.getId(), product));
        this.orderDetailsService = orderDetailsService;
    }


    public Map<Long, Product> addProduct(long id) {
        if(sessionStock.containsKey(id)) {
            int stockQuantity = sessionStock.get(id).getQuantity();
            if(stockQuantity > 0) {
                if(cart.containsKey(id)) {
                    cart.get(id).setQuantity(cart.get(id).getQuantity() + 1);
                } else {
                    Product cartProduct = new Product(sessionStock.get(id));
                    cartProduct.setQuantity(1);
                    cart.put(id, cartProduct);
                }
                sessionStock.get(id).setQuantity(stockQuantity - 1);
            }
        }
        return cart;
    }

    public Map<Long, Product> removeProduct(long id) {
        if(sessionStock.containsKey(id)) {
            int stockQuantity = sessionStock.get(id).getQuantity();
            if(cart.containsKey(id) && cart.get(id).getQuantity() > 0) {
                cart.get(id).setQuantity(cart.get(id).getQuantity() - 1);
                if(cart.get(id).getQuantity() == 0) {
                    cart.remove(id);
                }
            }
            sessionStock.get(id).setQuantity(stockQuantity + 1);
        }
        return cart;
    }

    public Map<Long, Product> getCart() {
        return cart;
    }

    public Map<Long, Product> getSessionStock() { return sessionStock; }

    public AtomicReference<String> checkout() {
        AtomicBoolean checkedOut = new AtomicBoolean(true);
        AtomicReference<String> message = null;
        // updating stock with the database to check against cart
        Map<Long, Product> inDatabaseStock = new LinkedHashMap<>();
        productRepository.findAll()
                .forEach(product -> inDatabaseStock.put(product.getId(), product));
        cart.values().forEach(product -> {
            if(inDatabaseStock.get(product.getId()).getQuantity() < product.getQuantity()) {
                checkedOut.set(false);
                message.set("Could not checkout. " + product.getName() + " is out of stock.");
                // remove product from cart and put back to sessionStock
                sessionStock.get(product.getId()).setQuantity(product.getQuantity());
                cart.remove(product.getId());
            }
        });
        if(checkedOut.get()) {
            sessionStock.values().forEach(productRepository::save);
            orderDetailsService.save(cart.values().stream().collect(Collectors.toList()));
            cart.clear();
            return null;
        } else {
            return message;
        }
    }

    public void deleteFromStock(long id) {
        sessionStock.remove(id);
    }

    public void addToStock(Product product) {
        sessionStock.put(product.getId(), product);
    }
}
