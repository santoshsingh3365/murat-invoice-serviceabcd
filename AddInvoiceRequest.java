package com.murat.invoice.generation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddInvoiceRequest {
    private long advertiserId;
    private String invoiceDate;
    private List<InvoiceProduct> products;
}
