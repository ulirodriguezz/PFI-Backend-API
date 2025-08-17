package com.example.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.OAuth2Utils;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;

@Configuration
public class FirebaseConfig {

    private final FirebaseConfigProps firebaseProperties;

    public FirebaseConfig(FirebaseConfigProps firebaseProperties) {
        this.firebaseProperties = firebaseProperties;
    }

//    @PostConstruct
//    public void init() {
//        try {
//            ServiceAccountCredentials credentials = ServiceAccountCredentials.fromPkcs8(
//                    firebaseProperties.getClientId(),
//                    firebaseProperties.getClientEmail(),
//                    firebaseProperties.getPrivateKey().replace("\\n", "\n"),
//                    firebaseProperties.getPrivateKeyId(),
//                    Collections.singleton("https://www.googleapis.com/auth/cloud-platform")
//            ).toBuilder().setProjectId(firebaseProperties.getPid()).build();
//
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(credentials)
//                    .setStorageBucket(firebaseProperties.getName())
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (Exception e) {
//            throw new RuntimeException("Error al inicializar Firebase", e);
//        }
//    }
//
//    @Bean
//    public Bucket firebaseBucket() {
//        // Usa el bucket por defecto que pusiste en FirebaseOptions.setStorageBucket(...)
//        return StorageClient.getInstance().bucket();
//        // Si querés forzar el nombre: return StorageClient.getInstance().bucket(firebaseProperties.getName());
//    }

}