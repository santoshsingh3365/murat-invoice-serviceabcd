package com.murat.invoice.generation.service;

import com.murat.invoice.generation.model.Advertiser;

import java.util.List;

public interface AdvertiserService {
    Advertiser addAdvertiser(Advertiser advertiser);

    Advertiser updateAdvertiser(Advertiser advertiser);

    void deleteAdvertiser(long advertiserId);

    List<Advertiser> searchAdvertiser(String searchTerm);
}
