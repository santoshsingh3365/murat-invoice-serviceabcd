package com.murat.invoice.generation.repositories;

import com.murat.invoice.generation.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "select p from Product p where (p.name like %:searchQuery% OR p.description like %:searchQuery% OR p.shotName like %:searchQuery% OR p.code like %:searchQuery%)")
    List<Product> searchProduct(@Param("searchQuery") String searchQuery);

    @Query(value = "select p from Product p where p.name = :productName")
    Product findByName(@Param("productName") String productName);
}
