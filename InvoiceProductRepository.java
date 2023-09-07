package com.murat.invoice.generation.repositories;

import com.murat.invoice.generation.model.InvoiceProduct;
import com.murat.invoice.generation.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    @Query(value = "select ip from InvoiceProduct ip where ip.invoiceId = :invoiceId")
    List<InvoiceProduct> getInvoiceProductByInvoiceId(@Param("invoiceId") long invoiceId);
}

