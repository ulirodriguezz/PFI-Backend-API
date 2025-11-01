package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rfid_readers")
public class RfidReader {
    @Id
    private String readerId;
    private boolean available;
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "wifi_config_id")
    private WifiConfigInfo wifiConfigInfo;

    public RfidReader() {
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public WifiConfigInfo getWifiConfigInfo() {
        return wifiConfigInfo;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void setWifiConfigInfo(WifiConfigInfo wifiConfigInfo) {
        this.wifiConfigInfo = wifiConfigInfo;
    }
}
