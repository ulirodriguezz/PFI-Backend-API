package com.example.demo.repository;

import com.example.demo.model.RfidReader;
import com.example.demo.model.Tenant;
import com.example.demo.model.WifiConfigInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReaderRepository extends JpaRepository<RfidReader, String> {
    @Modifying
    @Query("UPDATE RfidReader r SET r.available = false WHERE r.available=true AND r.tenant=:readerTenant")
    void clearAvailableReadersFromTenant(@Param("readerTenant") Tenant readerTenant);

    @Query("FROM RfidReader r WHERE r.available = true AND r.tenant =:tenant")
    List<RfidReader> getAllAvailableReadersByTenant(Tenant tenant);
}
