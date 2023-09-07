package com.murat.invoice.generation.repositories;

import com.murat.invoice.generation.model.Advertiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertiserRepository extends JpaRepository<Advertiser, Long> {

    @Query(value = "select a from Advertiser a where a.name like %:searchTerm%")
    List<Advertiser> searchAdvertiser(@Param("searchTerm") String searchTerm);

    @Query(value = "select a from Advertiser a where a.name = :advertiserName")
    Advertiser findByName(@Param("advertiserName") String advertiserName);
}
