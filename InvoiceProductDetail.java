package com.murat.invoice.generation.model.response;

import com.murat.invoice.generation.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceProductDetail {
    private Product product;
    private long quantity;
    private double rate;
    private double price;
}
