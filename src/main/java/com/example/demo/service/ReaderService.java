package com.example.demo.service;

import com.example.demo.dto.WifiConfigDTO;
import com.example.demo.model.RfidReader;
import com.example.demo.model.WifiConfigInfo;
import com.example.demo.repository.ReaderRepository;
import com.example.demo.repository.WifiConfigInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {
    private ReaderRepository readerRepository;
    private WifiConfigInfoRepository wifiConfigInfoRepository;

    public ReaderService(ReaderRepository readerRepository, WifiConfigInfoRepository wifiConfigInfoRepository) {
        this.readerRepository = readerRepository;
        this.wifiConfigInfoRepository = wifiConfigInfoRepository;
    }

    public List<RfidReader> getAllAvailableReaders(){
        return readerRepository.getAllAvailableReaders();
    }
    public RfidReader save (RfidReader reader){
        return readerRepository.save(reader);
    }
    @Transactional
    public void saveDefaultWifiConfig(WifiConfigDTO wifiInfo){

        WifiConfigInfo oldDefaultCOnfig = wifiConfigInfoRepository.getDefaultConfig();
        if(oldDefaultCOnfig != null)
            wifiConfigInfoRepository.delete(oldDefaultCOnfig);

        WifiConfigInfo wifiConfig = new WifiConfigInfo();
        wifiConfig.setWifiName(wifiInfo.getSsid());
        wifiConfig.setWifiPassword(wifiInfo.getPassword());
        wifiConfig.setDefault(true);
        wifiConfigInfoRepository.save(wifiConfig);
    }

    public WifiConfigDTO getWifiConfigFromReader(String mac) {
        RfidReader reader = readerRepository.findById(mac).orElse(new RfidReader());
        WifiConfigInfo wifiConfig = reader.getWifiConfigInfo();
        //If the reader did not have a wifi config set, use the default config
        if(wifiConfig == null)
            wifiConfig = wifiConfigInfoRepository.getDefaultConfig();

        WifiConfigDTO dto = new WifiConfigDTO();
        dto.setSsid(wifiConfig.getWifiName());
        dto.setPassword(wifiConfig.getWifiPassword());
        return dto;
    }

    public WifiConfigDTO getDefaultWifiConfig(){
        WifiConfigDTO dto = new WifiConfigDTO();
        WifiConfigInfo wifiConfigInfo = wifiConfigInfoRepository.getDefaultConfig();
        if(wifiConfigInfo == null)
            throw new EntityNotFoundException("Default config Not Found");
        dto.setSsid(wifiConfigInfo.getWifiName());
        dto.setPassword(wifiConfigInfo.getWifiPassword());
        return dto;
    }

    @Transactional
    public RfidReader updateOrSaveAvailableReader(RfidReader readerInfo) {
        Optional<RfidReader> existingReader = readerRepository.findById(readerInfo.getReaderId());

        readerRepository.clearAvailableReaders(); //Clear all available readers (Should always be 1 max) to mark this reader as available

        RfidReader reader = existingReader.orElseGet(() -> new RfidReader());
        reader.setReaderId(readerInfo.getReaderId());
        reader.setAvailable(true);


        return readerRepository.save(reader);
    }
    @Transactional
    public void updateReaderWifiConfig(WifiConfigDTO wifiConfigInfo,String mac){
        boolean didExist = true;
        WifiConfigInfo existingWifiConfig = wifiConfigInfoRepository.getByWifiName(wifiConfigInfo.getSsid()).orElse(new WifiConfigInfo());
        if(existingWifiConfig.getWifiName() == null)
            didExist = false;
        existingWifiConfig.setWifiName(wifiConfigInfo.getSsid());
        existingWifiConfig.setWifiPassword(wifiConfigInfo.getPassword());
        existingWifiConfig.setDefault(false);

        if(!didExist){
            RfidReader reader = readerRepository.findById(mac).orElseThrow(()-> new EntityNotFoundException("Reader Not Found"));
            reader.setWifiConfigInfo(existingWifiConfig);
            readerRepository.save(reader);
        }else{
            wifiConfigInfoRepository.save(existingWifiConfig);
        }
    }

    public void setReaderAsUnavailable(String readerId) {
        RfidReader rfidReader = readerRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Reader Not Found"));
        rfidReader.setAvailable(false);
        readerRepository.save(rfidReader);
    }
}
