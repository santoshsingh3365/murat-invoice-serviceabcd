package com.murat.invoice.generation.model.response;

import com.murat.invoice.generation.model.Advertiser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetail {
    private long invoiceId;
    private Advertiser advertiser;
    private String invoiceDate;
    private double invoiceAmount;
    private List<InvoiceProductDetail> productDetails;


}
