package com.murat.invoice.generation.controller;

import com.murat.invoice.generation.constants.ApplicationConstants;
import com.murat.invoice.generation.exception.InvalidRequest;
import com.murat.invoice.generation.model.AddInvoiceRequest;
import com.murat.invoice.generation.model.ApiResponse;
import com.murat.invoice.generation.model.InvoiceProduct;
import com.murat.invoice.generation.model.response.InvoiceDetail;
import com.murat.invoice.generation.service.InvoiceService;
import com.murat.invoice.generation.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping(value = "/addInvoice")
    public ResponseEntity<ApiResponse<Long>> addInvoice(@RequestBody AddInvoiceRequest invoiceRequest){
        validateInvoiceRequest(invoiceRequest);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ApplicationConstants.INVOICE_ADDED_SUCCESSFULLY, null, invoiceService.addInvoice(invoiceRequest)), HttpStatus.OK);
    }

    private void validateInvoiceRequest(AddInvoiceRequest invoiceRequest) {
        if(invoiceRequest==null || invoiceRequest.getAdvertiserId()==0 || invoiceRequest.getInvoiceDate()==null || invoiceRequest.getInvoiceDate().isBlank() || CollectionUtils.isEmpty(invoiceRequest.getProducts())){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }



        for(InvoiceProduct product : invoiceRequest.getProducts()){
            if(product.getProductId()==0 || product.getQuantity()==0){
                throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
            }
        }
    }

    @GetMapping(value = "/getInvoiceDetail")
    public ResponseEntity<ApiResponse<InvoiceDetail>> getInvoiceDetail(@RequestParam long invoiceId){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), null, null, invoiceService.getInvoiceDetail(invoiceId)), HttpStatus.OK);
    }
}
