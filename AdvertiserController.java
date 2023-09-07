package com.murat.invoice.generation.controller;

import com.murat.invoice.generation.constants.ApplicationConstants;
import com.murat.invoice.generation.exception.InvalidRequest;
import com.murat.invoice.generation.exception.ResourceNotFound;
import com.murat.invoice.generation.model.Advertiser;
import com.murat.invoice.generation.model.ApiResponse;
import com.murat.invoice.generation.service.AdvertiserService;
import com.murat.invoice.generation.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;


@RestController
@RequestMapping(path = "/advertiser")
public class AdvertiserController {

    @Autowired
    private AdvertiserService advertiserService;

    @GetMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<Advertiser>>> searchAdvertiser(@RequestParam String searchTerm){
        if(searchTerm==null || searchTerm.isBlank()){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        List<Advertiser> advertisers = advertiserService.searchAdvertiser(searchTerm);
        if(CollectionUtils.isEmpty(advertisers)){
            throw new ResourceNotFound(ApplicationConstants.ADVERTISER_NOT_EXISTS);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), null, null, advertisers), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponse<Advertiser>> addAdvertiser(@RequestBody Advertiser advertiser){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ApplicationConstants.ADVERTISER_ADDED_SUCCESSFULLY, null, advertiserService.addAdvertiser(advertiser)), HttpStatus.OK);
    }

    @PutMapping(value = "/save")
    public ResponseEntity<ApiResponse<Advertiser>> updateAdvertiser(@RequestBody Advertiser advertiser){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ApplicationConstants.ADVERTISER_UPDATED_SUCCESSFULLY, null, advertiserService.updateAdvertiser(advertiser)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ApiResponse<String>> deleteAdvertiser(@RequestParam long advertiserId){
        advertiserService.deleteAdvertiser(advertiserId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ApplicationConstants.ADVERTISER_DELETED_SUCCESSFULLY, null, null), HttpStatus.OK);
    }
}
