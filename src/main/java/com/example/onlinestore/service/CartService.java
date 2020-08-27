package com.example.onlinestore.service;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
@SessionScope
@Transactional
public class CartService {
    private final Map<Long, Product> cart = new LinkedHashMap<>();
    private final ProductRepository productRepository;
    private Map<Long, Product> stock = new LinkedHashMap<>();

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        productRepository.findAll()
                .forEach(product -> stock.put(product.getId(), product));
    }


    public Map<Long, Product> addProduct(long id) {
        if(stock.containsKey(id)) {
            int stockQuantity = stock.get(id).getQuantity();
            if(stockQuantity > 0) {
                if(cart.containsKey(id)) {
                    cart.get(id).setQuantity(cart.get(id).getQuantity() + 1);
                } else {
                    Product cartProduct = new Product(stock.get(id));
                    cartProduct.setQuantity(1);
                    cart.put(id, cartProduct);
                }
                stock.get(id).setQuantity(stockQuantity - 1);
            }
        }
        return cart;
    }

    public Map<Long, Product> removeProduct(long id) {
        if(stock.containsKey(id)) {
            int stockQuantity = stock.get(id).getQuantity();
            if(cart.containsKey(id) && cart.get(id).getQuantity() > 0) {
                cart.get(id).setQuantity(cart.get(id).getQuantity() - 1);
                if(cart.get(id).getQuantity() == 0) {
                    cart.remove(id);
                }
            }
            stock.get(id).setQuantity(stockQuantity + 1);
        }
        return cart;
    }

    public Map<Long, Product> getCart() {
        return cart;
    }

    public Map<Long, Product> getStock() { return stock; }

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
                cart.remove(product.getId());
            }
        });
        if(checkedOut.get()) {
            stock.values().forEach(productRepository::save);
            cart.clear();
            return null;
        } else {
            return message;
        }
    }

    public void deleteFromStock(long id) {
        stock.remove(id);
    }

    public void addToStock(Product product) {
        stock.put(product.getId(), product);
    }
}
