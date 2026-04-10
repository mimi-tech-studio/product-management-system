package com.mimitech.product.service;

import com.mimitech.product.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    private List<Product> products = new ArrayList<>();
    private long nextId = 1;
    
    public ProductService() {
        // 初始化一些测试产品
        initializeSampleProducts();
    }
    
    private void initializeSampleProducts() {
        // 电子产品
        addProduct("笔记本电脑", "高性能笔记本电脑，适合办公和游戏", 
                   new BigDecimal("5999.99"), 50, "电子产品", "LAPTOP001");
        
        addProduct("智能手机", "最新款智能手机，拍照功能强大", 
                   new BigDecimal("3999.99"), 100, "电子产品", "PHONE001");
        
        addProduct("平板电脑", "轻薄便携的平板电脑", 
                   new BigDecimal("2999.99"), 30, "电子产品", "TABLET001");
        
        // 办公家具
        addProduct("办公椅", "人体工学办公椅，保护脊椎健康", 
                   new BigDecimal("899.99"), 20, "办公家具", "CHAIR001");
        
        addProduct("办公桌", "实木办公桌，宽敞耐用", 
                   new BigDecimal("1299.99"), 15, "办公家具", "DESK001");
        
        // 图书
        addProduct("Java编程思想", "Java编程经典书籍", 
                   new BigDecimal("89.99"), 200, "图书", "BOOK001");
        
        addProduct("Spring Boot实战", "Spring Boot开发指南", 
                   new BigDecimal("79.99"), 150, "图书", "BOOK002");
        
        // 家电
        addProduct("空调", "节能静音空调", 
                   new BigDecimal("2599.99"), 25, "家电", "AC001");
        
        addProduct("洗衣机", "全自动洗衣机", 
                   new BigDecimal("1899.99"), 30, "家电", "WASHER001");
        
        addProduct("冰箱", "双开门大容量冰箱", 
                   new BigDecimal("3299.99"), 20, "家电", "FRIDGE001");
    }
    
    private void addProduct(String name, String description, BigDecimal price, 
                           Integer stock, String category, String sku) {
        Product product = new Product();
        product.setId(nextId++);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setSku(sku);
        product.setImageUrl("/images/products/" + sku.toLowerCase() + ".jpg");
        product.setIsActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        products.add(product);
    }
    
    public List<Product> getAllProducts() {
        return products.stream()
                .filter(Product::getIsActive)
                .toList();
    }
    
    public Product getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .filter(Product::getIsActive)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("产品不存在或已下架，ID: " + id));
    }
    
    public Product createProduct(Product product) {
        // 验证SKU唯一性
        if (products.stream().anyMatch(p -> p.getSku() != null && p.getSku().equals(product.getSku()))) {
            throw new RuntimeException("SKU已存在: " + product.getSku());
        }
        
        product.setId(nextId++);
        product.setIsActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        products.add(product);
        return product;
    }
    
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        
        boolean updated = false;
        
        if (productDetails.getName() != null && !productDetails.getName().equals(product.getName())) {
            product.setName(productDetails.getName());
            updated = true;
        }
        
        if (productDetails.getDescription() != null && !productDetails.getDescription().equals(product.getDescription())) {
            product.setDescription(productDetails.getDescription());
            updated = true;
        }
        
        if (productDetails.getPrice() != null && productDetails.getPrice().compareTo(product.getPrice()) != 0) {
            product.setPrice(productDetails.getPrice());
            updated = true;
        }
        
        if (productDetails.getStock() != null && !productDetails.getStock().equals(product.getStock())) {
            product.setStock(productDetails.getStock());
            updated = true;
        }
        
        if (productDetails.getCategory() != null && !productDetails.getCategory().equals(product.getCategory())) {
            product.setCategory(productDetails.getCategory());
            updated = true;
        }
        
        if (productDetails.getSku() != null && !productDetails.getSku().equals(product.getSku())) {
            // 验证新SKU唯一性
            if (products.stream().anyMatch(p -> p.getId() != id && p.getSku() != null && p.getSku().equals(productDetails.getSku()))) {
                throw new RuntimeException("SKU已存在: " + productDetails.getSku());
            }
            product.setSku(productDetails.getSku());
            updated = true;
        }
        
        if (productDetails.getImageUrl() != null && !productDetails.getImageUrl().equals(product.getImageUrl())) {
            product.setImageUrl(productDetails.getImageUrl());
            updated = true;
        }
        
        if (productDetails.getIsActive() != null && !productDetails.getIsActive().equals(product.getIsActive())) {
            product.setIsActive(productDetails.getIsActive());
            updated = true;
        }
        
        if (updated) {
            product.setUpdatedAt(LocalDateTime.now());
        }
        
        return product;
    }
    
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setIsActive(false);
        product.setUpdatedAt(LocalDateTime.now());
    }
    
    public List<Product> searchProducts(String keyword, String category, 
                                       BigDecimal minPrice, BigDecimal maxPrice) {
        return products.stream()
                .filter(Product::getIsActive)
                .filter(p -> keyword == null || 
                        p.getName().contains(keyword) || 
                        (p.getDescription() != null && p.getDescription().contains(keyword)))
                .filter(p -> category == null || category.equals(p.getCategory()))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }
    
    public Product updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        
        if (quantity < 0) {
            throw new RuntimeException("库存数量不能为负数");
        }
        
        product.setStock(quantity);
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }
    
    public void decreaseStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足，当前库存: " + product.getStock() + "，需求: " + quantity);
        }
        
        product.setStock(product.getStock() - quantity);
        product.setUpdatedAt(LocalDateTime.now());
    }
    
    public void increaseStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        
        if (quantity < 0) {
            throw new RuntimeException("增加数量不能为负数");
        }
        
        product.setStock(product.getStock() + quantity);
        product.setUpdatedAt(LocalDateTime.now());
    }
    
    public List<String> getAllCategories() {
        return products.stream()
                .filter(Product::getIsActive)
                .map(Product::getCategory)
                .distinct()
                .toList();
    }
    
    public long getTotalProductCount() {
        return products.stream()
                .filter(Product::getIsActive)
                .count();
    }
    
    public BigDecimal getTotalInventoryValue() {
        return products.stream()
                .filter(Product::getIsActive)
                .map(p -> p.getPrice().multiply(new BigDecimal(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public List<Product> getLowStockProducts(int threshold) {
        return products.stream()
                .filter(Product::getIsActive)
                .filter(p -> p.getStock() <= threshold)
                .toList();
    }
}
