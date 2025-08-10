package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "firebase.bucket")
public class FirebaseConfigProps {
    public String clientId;
    private String pid;
    private String clientEmail;
    private String privateKey;
    private String privateKeyId;
    private String name;


    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }

    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }

    public String getPrivateKeyId() { return privateKeyId; }
    public void setPrivateKeyId(String privateKeyId) { this.privateKeyId = privateKeyId; }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
