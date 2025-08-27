package com.example.demo.service;

import com.example.demo.dto.ImageDTO;
import com.example.demo.exception.FileException;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.model.Container;
import com.example.demo.model.Image;
import com.example.demo.model.Item;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final FirebaseStorageService firebaseStorageService;

    private final ImageRepository imageRepository;

    private final ItemRepository itemRepository;

    private final ContainerRepository containerRepository;

    private final ImageMapper imageMapper;

    public ImageService(FirebaseStorageService firebaseStorageService, ImageRepository imageRepository, ItemRepository itemRepository, ContainerRepository containerRepository, ImageMapper imageMapper) {
        this.firebaseStorageService = firebaseStorageService;
        this.imageRepository = imageRepository;
        this.itemRepository = itemRepository;
        this.containerRepository = containerRepository;
        this.imageMapper = imageMapper;
    }

    public ImageDTO addImageToItem(MultipartFile imageMPF, long itemId, String folderName){
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));

        Image image = new Image();
        image.setItem(item);
        saveFileToStorageAndMutateImage(imageMPF,folderName,image);

        return imageMapper.toImageDTO(imageRepository.save(image));

    }
    public ImageDTO addImageToContainer(MultipartFile imageMPF, long containerId, String folderName){
        Container container = containerRepository.getContainerById(containerId)
                .orElseThrow(() -> new EntityNotFoundException("Container Not Found"));

        Image image = new Image();
        image.setContainer(container);
        saveFileToStorageAndMutateImage(imageMPF,folderName,image);

        return imageMapper.toImageDTO(imageRepository.save(image));
    }

    public ImageDTO saveImage(MultipartFile imageMPF, String folderName, Image image){
        saveFileToStorageAndMutateImage(imageMPF,folderName,image);
        return imageMapper.toImageDTO(imageRepository.save(image));
    }

    public void deleteImageFromItem(long imageId, long itemId){
        Image image = imageRepository.getImageById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image Not Found"));
        if(image.getItem().getId() != itemId)
            throw new EntityNotFoundException("Image Not Found");
        deleteImage(image);
    }
    public void deleteImageFromContainer(long imageId, long containerId){
        Image image = imageRepository.getImageById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image Not Found"));
        if(image.getContainer().getId() != containerId)
            throw new EntityNotFoundException("Image Not Found");
        deleteImage(image);
    }

    public List<ImageDTO> getAllImagesByItemId(long itemId){
        List<Image> imageList = imageRepository.getAllByItemIdOrderByCreatedAtAsc(itemId);
        return imageMapper.toImageDtoList(imageList);
    }
    public List<ImageDTO> getAllImagesByContainerId(long cotainerId){
        List<Image> imageList = imageRepository.getAllByContainerIdOrderByCreatedAtAsc(cotainerId);
        return imageMapper.toImageDtoList(imageList);
    }

    private String saveFileToStorageAndMutateImage(MultipartFile imageMPF,String folderName, Image image){
        String prefix = (folderName == null || folderName.isBlank()) ? "uploads" : folderName.replaceAll("^/|/$", "");
        String fileName = prefix + "/" + UUID.randomUUID().toString();
        String url;
        try{
            url = firebaseStorageService.uploadFile(imageMPF,fileName,folderName);
        }catch (Exception e){
            throw new FileException("Error uploading File");
        }
        image.setFileName(fileName);
        image.setImageURL(url);
        return url;
    }
    private void deleteImage(Image image){
        try{
            firebaseStorageService.deleteFile(image.getFileName());
        }catch (Exception e){
            throw new FileException("Error Deleting File");
        }
        imageRepository.delete(image);
    }

}
