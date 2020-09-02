package com.example.onlinestore.service;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import com.example.onlinestore.utlis.ShoppingCart;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@SessionScope
@Transactional
public class CartService {
    private final ShoppingCart shoppingCart;
//    private final Map<Long, Product> cart = new LinkedHashMap<>();
    private final ProductRepository productRepository;
    private final OrderDetailsService orderDetailsService;
    private Map<Long, Product> sessionStock = new LinkedHashMap<>();

    public CartService(ProductRepository productRepository, OrderDetailsService orderDetailsService) {
        this.productRepository = productRepository;
        productRepository.findAll()
                .forEach(product -> sessionStock.put(product.getId(), product));
        this.orderDetailsService = orderDetailsService;
        shoppingCart = new ShoppingCart();
    }


    public Map<Long, Product> addProduct(long id) {
        if(sessionStock.containsKey(id)) {
            int stockQuantity = sessionStock.get(id).getQuantity();
            if(stockQuantity > 0) {
                if(shoppingCart.getProducts().containsKey(id)) {
                    shoppingCart.getProducts().get(id).setQuantity(shoppingCart.getProducts().get(id).getQuantity() + 1);
                } else {
                    Product cartProduct = new Product(sessionStock.get(id));
                    cartProduct.setQuantity(1);
                    shoppingCart.getProducts().put(id, cartProduct);
                }
                sessionStock.get(id).setQuantity(stockQuantity - 1);
                shoppingCart.setTotal(shoppingCart.getTotal() + shoppingCart.getProducts().get(id).getPrice());
            }
        }
        return shoppingCart.getProducts();
    }

    public Map<Long, Product> removeProduct(long id) {
        if(sessionStock.containsKey(id)) {
            int stockQuantity = sessionStock.get(id).getQuantity();
            if(shoppingCart.getProducts().containsKey(id) && shoppingCart.getProducts().get(id).getQuantity() > 0) {
                shoppingCart.getProducts().get(id).setQuantity(shoppingCart.getProducts().get(id).getQuantity() - 1);
                shoppingCart.setTotal(shoppingCart.getTotal() - shoppingCart.getProducts().get(id).getPrice());
                if(shoppingCart.getProducts().get(id).getQuantity() == 0) {
                    shoppingCart.getProducts().remove(id);
                    shoppingCart.setTotal(0.0);
                }
            }
            sessionStock.get(id).setQuantity(stockQuantity + 1);
        }
        return shoppingCart.getProducts();
    }

    public ShoppingCart getCart() {
        return shoppingCart;
    }

    public Map<Long, Product> getSessionStock() { return sessionStock; }

    public long checkout() {
        long id = -1;
        List<Long> toRemove = new ArrayList<>();
        AtomicBoolean checkedOut = new AtomicBoolean(true);
        // updating stock with the database to check against cart
        Map<Long, Product> inDatabaseStock = new LinkedHashMap<>();
        if(shoppingCart.isEmpty()) {
            return id;
        }
        productRepository.findAll()
                .forEach(product -> inDatabaseStock.put(product.getId(), product));
        shoppingCart.getProducts().values().forEach(product -> {
            if(inDatabaseStock.get(product.getId()).getQuantity() < product.getQuantity()) {
                checkedOut.set(false);
                // remove product from cart and put back to sessionStock
                toRemove.add(product.getId());
                // update session stock
                productRepository.findAll()
                        .forEach(pr -> sessionStock.put(pr.getId(), pr));
            }
        });
        // to avoid ConcurrentModificationException,
        // remove the products from shopping cart after iterating over it
        toRemove.forEach(productId -> shoppingCart.getProducts().remove(productId));
        if(checkedOut.get()) {
            sessionStock.values().forEach(product -> {
                if(shoppingCart.getProducts().containsKey(product.getId())) {
                    product.setArchived(true);
                }
                productRepository.save(product);
            });
            id = orderDetailsService.save(new ArrayList<>(shoppingCart.getProducts().values()));
            shoppingCart.getProducts().clear();
            shoppingCart.setTotal(0.0);
        }
        return id;
    }

    public String updateSessionStock() {
        String returnMessage = null;
        Map<Long, Product> inDatabaseStock = new LinkedHashMap<>();
        if(shoppingCart.isEmpty()) {
            productRepository.findAll()
                    .forEach(product -> sessionStock.put(product.getId(), product));
        } else {
            List<Long> toRemove = new ArrayList<>();
            List<String> outOfStockItems = new ArrayList<>();
            productRepository.findAll()
                    .forEach(product -> inDatabaseStock.put(product.getId(), product));
            shoppingCart.getProducts().values().forEach(product -> {
                if(inDatabaseStock.get(product.getId()).getQuantity() < product.getQuantity()) {
                    // remove product from cart and put back to sessionStock
                    outOfStockItems.add(product.getName());
                    toRemove.add(product.getId());
                }
            });
            toRemove.forEach(productId -> shoppingCart.getProducts().remove(productId));
            if(!outOfStockItems.isEmpty()) {
                returnMessage = "Sorry! Items " + outOfStockItems.toString() + " are out of stock. Cart updated.";
            }
        }
        return returnMessage;

    }

    public void deleteFromStock(long id) {
        sessionStock.remove(id);
    }

    public void addToStock(Product product) {
        sessionStock.put(product.getId(), product);
    }

    public void editInStock(Product product) {
        sessionStock.replace(product.getId(), product);
    }
}
