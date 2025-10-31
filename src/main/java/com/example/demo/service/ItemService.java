package com.example.demo.service;

import com.example.demo.dto.FullItemDTO;
import com.example.demo.dto.ImageDTO;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Container;
import com.example.demo.model.Image;
import com.example.demo.model.Item;
import com.example.demo.model.User;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ContainerRepository containerRepository;

    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    private final MovementService movementService;

    private final ImageService imageService;

    public ItemService(ItemRepository itemRepository,
                       ItemMapper itemMapper,
                       ContainerRepository containerRepository,
                       MovementService movementService,
                       UserRepository userRepository,
                       ImageService imageService
    ) {


        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.containerRepository = containerRepository;
        this.movementService = movementService;
        this.userRepository = userRepository;
        this.imageService = imageService;

    }

    @Transactional
    public SimpleItemDTO saveItem(SimpleItemDTO itemDto) {
        if (itemDto.getContainerId() != null)
            return this.saveItemInContainer(itemDto, itemDto.getContainerId());
        Item savedItem = this.itemRepository.save(itemMapper.toItemEntity(itemDto));
        return itemMapper.toSimpleItemDTO(savedItem);
    }

    @Transactional
    public SimpleItemDTO saveItemInContainer(SimpleItemDTO itemData, long containerId) {
        Item newItem = itemMapper.toItemEntity(itemData);
        Container container = containerRepository.getContainerById(containerId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));
        newItem.setContainer(container);
        Item savedItem = itemRepository.save(newItem);
        return itemMapper.toSimpleItemDTO(savedItem);
    }

    public FullItemDTO getItemById(String username, long id) {
        Item item = itemRepository.getItemById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado"));

        boolean isUserFavoriteItem = itemRepository.isItemFavorite(username, id);

        return itemMapper.toFullItemDTO(item, isUserFavoriteItem);
    }

    public Item getItemByName(String name) {
        Item item = itemRepository.getItemByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado"));
        return item;
    }

    public List<Item> filterItems(String query) {
        List<Item> results = itemRepository.searchAllByNameLike(query);
        if (results.isEmpty())
            throw new EntityNotFoundException("No se encontraron items para la busqueda: " + query);
        return results;
    }

    @Transactional
    public void deleteItem(long itemId) {
        movementService.deleteAllMovementsByItemId(itemId);
        imageService.deleteAllImagesByItemId(itemId);
        imageService.deleteAllReferencesToItem(itemId);
        itemRepository.deleteById(itemId);

    }

    @Transactional
    public Item updateItem(long id, SimpleItemDTO itemData) {
        Item storedItem = itemRepository.getItemById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el item"));

        if (itemData.getContainerId() != null) {
            Container destination = containerRepository.getContainerById(itemData.getContainerId())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontro el contendor"));
            movementService.registerNewMovement(storedItem, destination);
            storedItem.setContainer(destination);
            storedItem.setInUseBy(null);
        }
        itemMapper.mergeChanges(storedItem, itemData);
        return itemRepository.save(storedItem);
    }

    public SimpleItemDTO markInUse(String tagId, String username) {
        Item storedItem = itemRepository.getByTagId(tagId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el item"));
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario"));
        storedItem.setInUseBy(user);
        return itemMapper.toSimpleItemDTO(itemRepository.save(storedItem));
    }

    public List<SimpleItemDTO> getItemsByContainerId(long containerId) {
        List<Item> result = itemRepository.getItemByContainerId(containerId);
        return itemMapper.toSimpleItemDtoList(result);
    }

    @Transactional
    public ImageDTO addImageToItem(long itemId, MultipartFile imageMPF) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));

        Image image = new Image();
        image.setItem(item);
        return imageService.saveImage(imageMPF, "item-images", image);
    }

    @Transactional
    public void deleteImageFromItem(long imageId, long itemId) {
        imageService.deleteImageFromItem(imageId, itemId);
    }

    public List<ImageDTO> getAllImagesFromItem(long itemId) {
        return imageService.getAllImagesByItemId(itemId);
    }

    @Transactional
    public SimpleItemDTO assignRfidTagToItem(long itemId, String tagId) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        Item itemWithTagAlreadyAssigned = itemRepository.getByTagId(tagId)
                .orElse(null);
        //If the tagId was already assigned to another item, set it to null
        //and assign it to the newest item
        if (itemWithTagAlreadyAssigned != null) {
            itemWithTagAlreadyAssigned.setTagId(null);
            itemRepository.save(itemWithTagAlreadyAssigned);
        }
        item.setTagId(tagId);
        Item updatedItem = itemRepository.save(item);
        return itemMapper.toSimpleItemDTO(updatedItem);
    }

    public SimpleItemDTO getItemByTagId(String tagId) {
        Item item = itemRepository.getByTagId(tagId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el item"));
        return itemMapper.toSimpleItemDTO(item);

    }
}
