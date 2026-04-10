package com.mimitech.product.controller;

import com.mimitech.product.model.Product;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    // 内存存储（简化版）
    private List<Product> products = new ArrayList<>();
    private long nextId = 1;
    
    // 初始化一些测试数据
    public ProductController() {
        // 添加一些示例产品
        Product p1 = new Product();
        p1.setId(nextId++);
        p1.setName("笔记本电脑");
        p1.setDescription("高性能笔记本电脑");
        p1.setPrice(new BigDecimal("5999.99"));
        p1.setStock(50);
        p1.setCategory("电子产品");
        p1.setSku("PROD001");
        products.add(p1);
        
        Product p2 = new Product();
        p2.setId(nextId++);
        p2.setName("智能手机");
        p2.setDescription("最新款智能手机");
        p2.setPrice(new BigDecimal("3999.99"));
        p2.setStock(100);
        p2.setCategory("电子产品");
        p2.setSku("PROD002");
        products.add(p2);
        
        Product p3 = new Product();
        p3.setId(nextId++);
        p3.setName("办公椅");
        p3.setDescription("人体工学办公椅");
        p3.setPrice(new BigDecimal("899.99"));
        p3.setStock(30);
        p3.setCategory("办公家具");
        p3.setSku("PROD003");
        products.add(p3);
    }
    
    // 获取所有产品
    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }
    
    // 根据ID获取产品
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("产品不存在，ID: " + id));
    }
    
    // 创建新产品
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        product.setId(nextId++);
        products.add(product);
        return product;
    }
    
    // 更新产品
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product product = getProductById(id);
        
        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getStock() != null) {
            product.setStock(productDetails.getStock());
        }
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }
        if (productDetails.getSku() != null) {
            product.setSku(productDetails.getSku());
        }
        if (productDetails.getImageUrl() != null) {
            product.setImageUrl(productDetails.getImageUrl());
        }
        
        return product;
    }
    
    // 删除产品
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        Product product = getProductById(id);
        product.setIsActive(false);
    }
    
    // 搜索产品
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        return products.stream()
                .filter(p -> p.getIsActive())
                .filter(p -> keyword == null || 
                        p.getName().contains(keyword) || 
                        p.getDescription().contains(keyword))
                .filter(p -> category == null || category.equals(p.getCategory()))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }
    
    // 更新库存
    @PatchMapping("/{id}/stock")
    public Product updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        Product product = getProductById(id);
        product.setStock(quantity);
        return product;
    }
}
