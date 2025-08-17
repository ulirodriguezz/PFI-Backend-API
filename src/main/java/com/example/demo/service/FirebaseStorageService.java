package com.example.demo.service;


import com.example.demo.config.FirebaseConfig;
import com.example.demo.config.FirebaseConfigProps;
import com.example.demo.model.Image;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class FirebaseStorageService {

//    private final Bucket bucket;
//
//    private final FirebaseConfigProps firebaseConfigProps;
//
//    public FirebaseStorageService(Bucket bucket, FirebaseConfigProps firebaseConfigProps) {
//        this.bucket = bucket;
//        this.firebaseConfigProps =firebaseConfigProps;
//    }
//
//    public void deleteFile(String objectName){
//        bucket.getStorage().delete(bucket.getName(),objectName);
//    }
//
//
//    public String uploadFile(MultipartFile file,String objectName, String folder) throws IOException {
//
//        String id = UUID.randomUUID().toString();
//        Storage storage = bucket.getStorage(); // cliente ya autenticado
//        BlobInfo info = BlobInfo.newBuilder(bucket.getName(), objectName)
//                .setContentType(file.getContentType())
//                .setCacheControl("public,max-age=31536000,immutable")
//                .setMetadata(Map.of("firebaseStorageDownloadTokens", id))
//                .build();
//
//        storage.create(info, file.getBytes());
//
//        String path = URLEncoder.encode(objectName, StandardCharsets.UTF_8);
//        String url = "https://firebasestorage.googleapis.com/v0/b/" + bucket.getName()
//                + "/o/" + path + "?alt=media&token=" + id;
//
//        // Generar URL pública
//        return url;
//    }

}
