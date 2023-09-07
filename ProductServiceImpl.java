package com.murat.invoice.generation.service.impl;

import com.murat.invoice.generation.constants.ApplicationConstants;
import com.murat.invoice.generation.exception.InvalidRequest;
import com.murat.invoice.generation.exception.ResourceAlreadyExists;
import com.murat.invoice.generation.exception.ResourceNotFound;
import com.murat.invoice.generation.model.Advertiser;
import com.murat.invoice.generation.model.Product;
import com.murat.invoice.generation.repositories.AdvertiserRepository;
import com.murat.invoice.generation.repositories.ProductRepository;
import com.murat.invoice.generation.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product){
        if(product==null || product.getId()>0 || product.getName()==null || product.getName().isBlank()){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        Product existingProduct = productRepository.findByName(product.getName());
        if(existingProduct!=null){
            throw new ResourceAlreadyExists(ApplicationConstants.PRODUCT_ALREADY_EXISTS);
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product){
        if(product==null || product.getId()==0 || product.getName()==null || product.getName().isBlank()){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        if(!productRepository.findById(product.getId()).isPresent()){
            throw new ResourceNotFound(ApplicationConstants.PRODUCT_NOT_EXISTS);
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(long productId){
        if(productId==0){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        if(!productRepository.findById(productId).isPresent()){
            throw new ResourceNotFound(ApplicationConstants.PRODUCT_NOT_EXISTS);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> searchProduct(String searchString){
        return productRepository.searchProduct(searchString);
    }
}
