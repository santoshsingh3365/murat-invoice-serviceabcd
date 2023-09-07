package com.murat.invoice.generation.service.impl;

import com.murat.invoice.generation.constants.ApplicationConstants;
import com.murat.invoice.generation.exception.InvalidRequest;
import com.murat.invoice.generation.exception.ResourceAlreadyExists;
import com.murat.invoice.generation.exception.ResourceNotFound;
import com.murat.invoice.generation.model.Advertiser;
import com.murat.invoice.generation.repositories.AdvertiserRepository;
import com.murat.invoice.generation.service.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertiserServiceImpl implements AdvertiserService {
    @Autowired
    private AdvertiserRepository advertiserRepository;

    @Override
    public Advertiser addAdvertiser(Advertiser advertiser){
        if(advertiser==null || advertiser.getId()>0 || advertiser.getName()==null || advertiser.getName().isBlank()){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        Advertiser existingAdvertiser = advertiserRepository.findByName(advertiser.getName());
        if(existingAdvertiser!=null){
            throw new ResourceAlreadyExists(ApplicationConstants.ADVERTISER_ALREADY_EXISTS);
        }
        return advertiserRepository.save(advertiser);
    }

    @Override
    public Advertiser updateAdvertiser(Advertiser advertiser){
        if(advertiser==null || advertiser.getId()==0 || advertiser.getName()==null || advertiser.getName().isBlank()){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        if(!advertiserRepository.findById(advertiser.getId()).isPresent()){
            throw new ResourceNotFound(ApplicationConstants.ADVERTISER_NOT_EXISTS);
        }
        return advertiserRepository.save(advertiser);
    }

    @Override
    public void deleteAdvertiser(long advertiserId){
        if(advertiserId==0){
            throw new InvalidRequest(ApplicationConstants.PROVIDE_MANDATORY_PARAM);
        }
        if(!advertiserRepository.findById(advertiserId).isPresent()){
            throw new ResourceNotFound(ApplicationConstants.ADVERTISER_NOT_EXISTS);
        }
        advertiserRepository.deleteById(advertiserId);
    }

    @Override
    public List<Advertiser> searchAdvertiser(String searchTerm){
        return advertiserRepository.searchAdvertiser(searchTerm);
    }
}
