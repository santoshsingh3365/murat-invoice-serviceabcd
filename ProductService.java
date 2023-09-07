package com.murat.invoice.generation.service;

import com.murat.invoice.generation.model.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(long productId);

    List<Product> searchProduct(String searchString);
}
