package com.murat.invoice.generation.controller;

import com.murat.invoice.generation.constants.ApplicationConstants;
import com.murat.invoice.generation.exception.InvalidRequest;
import com.murat.invoice.generation.exception.ResourceNotFound;
import com.murat.invoice.generation.model.Advertiser;
import com.murat.invoice.generation.model.ApiResponse;
import com.murat.invoice.generation.model.Product;
import com.murat.invoice.generation.service.ProductService;
import com.murat.invoice.generation.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;


@RestController
@RequestMapping(path = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProduct(@RequestParam String searchTerm){
        if(searchTerm==null || searchTerm.isBlank()){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        List<Product> products = productService.searchProduct(searchTerm);
        if(CollectionUtils.isEmpty(products)){
            throw new ResourceNotFound(ApplicationConstants.PRODUCT_NOT_EXISTS);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), null, null, products), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponse<Product>> addProduct(@RequestBody Product product){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ApplicationConstants.PRODUCT_ADDED_SUCCESSFULLY, null, productService.addProduct(product)), HttpStatus.OK);
    }

    @PutMapping(value = "/save")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@RequestBody Product product){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ApplicationConstants.PRODUCT_UPDATED_SUCCESSFULLY, null, productService.updateProduct(product)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@RequestParam long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ApplicationConstants.PRODUCT_DELETED_SUCCESSFULLY, null, null), HttpStatus.OK);
    }
}
