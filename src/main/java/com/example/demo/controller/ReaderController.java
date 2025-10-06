package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.WifiConfigDTO;
import com.example.demo.model.RfidReader;
import com.example.demo.service.ReaderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReaderController {

    private ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }
    @PostMapping("/readers/available")
    public ResponseEntity<RfidReader> addAvailableReader(@RequestBody RfidReader reader){
        RfidReader savedReader = readerService.updateOrSaveAvailableReader(reader);
        return ResponseEntity.ok(savedReader);
    }
    @GetMapping("/readers/available")
    public ResponseEntity<RfidReader> getAvailableReader(){
        //Should always be only one available reader
        List<RfidReader> availableReaders = readerService.getAllAvailableReaders();
        if(availableReaders.isEmpty())
            throw new EntityNotFoundException("No avaialable readers found");
        RfidReader availableReader = availableReaders.get(0);
        return ResponseEntity.ok(availableReader);
    }
    @GetMapping("/readers/wifi-config")
    public ResponseEntity<WifiConfigDTO>getReaderWifiConfig(@RequestParam(required = false) String mac){
        WifiConfigDTO wifiConfigDTO;
        if(mac == null || mac.isEmpty()){
            wifiConfigDTO = readerService.getDefaultWifiConfig();
        }else{
            wifiConfigDTO = readerService.getWifiConfigFromReader(mac);
        }
        return ResponseEntity.ok(wifiConfigDTO);
    }
    @PostMapping("/readers/wifi-config")
    public ResponseEntity<Message>addReaderWifiConfig(
            @RequestParam(required = false) String mac,
            @RequestParam(required = false, defaultValue = "false") boolean isDefault,
            @RequestBody WifiConfigDTO wifiConfigInfo

    ){
        String msg ="";
        if(mac == null || mac.isEmpty()){
            readerService.saveDefaultWifiConfig(wifiConfigInfo);
            msg = "Wifi por defecto actualizado";
        }else {
            readerService.updateReaderWifiConfig(wifiConfigInfo,mac);
            msg="Wifi actualizado exitosamente";
        }

        return ResponseEntity.ok(new Message(msg));
    }

}
