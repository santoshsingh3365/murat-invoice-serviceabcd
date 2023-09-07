package com.murat.invoice.generation.service;

import com.murat.invoice.generation.model.AddInvoiceRequest;
import com.murat.invoice.generation.model.response.InvoiceDetail;

public interface InvoiceService {
    long addInvoice(AddInvoiceRequest invoiceRequest);

    InvoiceDetail getInvoiceDetail(long invoiceId);

    void generateInvoicePdf(long invoiceId);
}
