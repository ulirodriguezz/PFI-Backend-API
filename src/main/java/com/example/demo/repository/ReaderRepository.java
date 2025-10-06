package com.example.demo.repository;

import com.example.demo.model.RfidReader;
import com.example.demo.model.WifiConfigInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReaderRepository extends JpaRepository<RfidReader, String> {
    @Query("FROM RfidReader WHERE available = true")
    List<RfidReader> getAllAvailableReaders();
    @Modifying
    @Query("UPDATE RfidReader SET available = false WHERE available=true")
    void clearAvailableReaders();

}
