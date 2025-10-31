package com.example.demo.repository;

import com.example.demo.model.WifiConfigInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WifiConfigInfoRepository extends JpaRepository<WifiConfigInfo,Long> {

    @Query("FROM WifiConfigInfo WHERE isDefault=true")
    WifiConfigInfo getDefaultConfig();
    Optional<WifiConfigInfo> getByWifiName(String wifiName);
}
