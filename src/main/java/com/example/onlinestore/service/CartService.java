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
    private final ProductRepository productRepository;
    private final OrderDetailsService orderDetailsService;
    private final ProductService productService;

    public CartService(ProductRepository productRepository, OrderDetailsService orderDetailsService, ProductService productService) {
        this.productRepository = productRepository;
        this.orderDetailsService = orderDetailsService;
        this.productService = productService;
        shoppingCart = new ShoppingCart();
    }


    public void addProduct(long id) {
        if(productService.get(id).isPresent()) {
            Product stockProduct = productService.get(id).get();
            shoppingCart.addProduct(stockProduct);
        }
    }

    public boolean removeProduct(long id) {
        if(productService.get(id).isPresent()) {
            Product stockProduct = productService.get(id).get();
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
            if(productService.get(product.getId()).isPresent()) {
                if(productService.get(product.getId()).get().getQuantity() < product.getQuantity()) {
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
                    .forEach(product -> {
                        Product toSave = new Product(productService.get(product.getId()).get());
                        toSave.setQuantity(toSave.getQuantity() - product.getQuantity());
                        productRepository.save(toSave);
                    });
            id = orderDetailsService.save(new ArrayList<>(shoppingCart.getProducts().values()));
            shoppingCart.getProducts().clear();
            shoppingCart.setTotal(0.0);
        }
        return id;
    }
//
//    public String updateSessionStock() {
//        String returnMessage = null;
//        Map<Long, Product> inDatabaseStock = new LinkedHashMap<>();
//        if(shoppingCart.isEmpty()) {
//            productRepository.findAll()
//                    .forEach(product -> sessionStock.put(product.getId(), product));
//        } else {
//            List<Long> toRemove = new ArrayList<>();
//            List<String> outOfStockItems = new ArrayList<>();
//            productRepository.findAll()
//                    .forEach(product -> inDatabaseStock.put(product.getId(), product));
//            shoppingCart.getProducts().values().forEach(product -> {
//                if(inDatabaseStock.get(product.getId()).getQuantity() < product.getQuantity()) {
//                    // remove product from cart and put back to sessionStock
//                    outOfStockItems.add(product.getName());
//                    toRemove.add(product.getId());
//                }
//            });
//            toRemove.forEach(productId -> shoppingCart.getProducts().remove(productId));
//            if(!outOfStockItems.isEmpty()) {
//                returnMessage = "Sorry! Items " + outOfStockItems.toString() + " are out of stock. Cart updated.";
//            }
//        }
//        return returnMessage;
//
//    }


}
