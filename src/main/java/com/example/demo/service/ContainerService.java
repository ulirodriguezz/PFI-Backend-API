package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.helpers.TenantContext;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ContainerService {
    private final ContainerRepository containerRepository;
    private final ContainerMapper containerMapper;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final SectorRepository sectorRepository;
    private final ImageService imageService;
    private ReaderService readerService;
    private final MovementRepository movementRepository;
    private final TenantRepository tenantRepository;

    public ContainerService(ContainerRepository containerRepository, ContainerMapper containerMapper, ItemMapper itemMapper, ItemRepository itemRepository, SectorRepository sectorRepository, ImageService imageService, ReaderService readerService, MovementRepository movementRepository, TenantRepository tenantRepository) {
        this.containerRepository = containerRepository;
        this.containerMapper = containerMapper;
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.sectorRepository = sectorRepository;
        this.imageService = imageService;
        this.readerService = readerService;
        this.movementRepository = movementRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<Container> getAllContainers() {
        //TenantService getTenantById()
        List<Container> results = containerRepository.findContainersByTenantId(1);
        if (results.isEmpty()) throw new EntityNotFoundException("No hay contenedores");
        return results;
    }

    @Transactional
    public FullContainerDTO getContainerById(long id) {
        Container storedContainer = containerRepository.getContainerById(id).orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));
        FullContainerDTO dto = containerMapper.toFullContainerDTO(storedContainer);

        List<Item> containerItems = itemRepository.getItemByContainerId(storedContainer.getId());

        dto.setItems(itemMapper.toItemPreviewList(containerItems));
        return dto;
    }

    public List<SimpleContainerDTO> getFilteredContainers(String query) {
        if (TenantContext.getTenantId() != 1) return getFilteredContainersByTenant(query);

        List<Container> results = containerRepository.findAllByNameContainsOrDescriptionContaining(query, query);
        if (results.isEmpty())
            throw new EntityNotFoundException("No se encontraron containers para la búsqueda: " + query);
        return containerMapper.toSimpleDTOList(results);
    }

    private List<SimpleContainerDTO> getFilteredContainersByTenant(String query) {
        Tenant userTenant = tenantRepository.findById(TenantContext.getTenantId()).orElseThrow(() -> new EntityNotFoundException("Tenant No Encontado"));

        List<Container> results = containerRepository.findAllByQueryAndTenant(query, userTenant);
        if (results.isEmpty())
            throw new EntityNotFoundException("No se encontraron containers para la búsqueda: " + query);

        return containerMapper.toSimpleDTOList(results);
    }

    @Transactional
    public SimpleContainerDTO save(SimpleContainerDTO newContainerData) {
        Tenant userTenant = tenantRepository.findById(TenantContext.getTenantId()).orElseThrow(() -> new EntityNotFoundException("Tenant No Encontrado"));

        Container newContainer = containerMapper.toContainerEntity(newContainerData);
        newContainer.setTenant(userTenant);
        return containerMapper.toSimpleDTO(containerRepository.save(newContainer));
    }

    @Transactional
    public SimpleContainerDTO updateContainer(long id, SimpleContainerDTO changedData) {
        Container oldContainer = containerRepository.getContainerById(id).orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));


        if (changedData.getSectorId() != null) {
            this.changeContainerSector(oldContainer, changedData.getSectorId());
        }
        if (changedData.getReaderId() != null) {
            readerService.setReaderAsUnavailable(changedData.getReaderId());
            containerRepository.clearReaderReferenceFromContainers(changedData.getReaderId());
        }
        containerMapper.mergeChanges(oldContainer, changedData);

        Container updatedContainer = containerRepository.save(oldContainer);
        return containerMapper.toSimpleDTO(updatedContainer);
    }

    @Transactional
    public void deleteById(long id) {
        movementRepository.removeAllReferencesToContainer(id);
        itemRepository.clearContainerReferenceFromItems(id);
        imageService.deleteAllImagesByContainerId(id);
        containerRepository.deleteById(id);
    }

    @Transactional
    public SimpleContainerDTO saveContainerInSector(long sectorId, SimpleContainerDTO containerData) {
        Sector sector = sectorRepository.getSectorById(sectorId).orElseThrow(() -> new EntityNotFoundException("Sector Not Found"));
        Tenant userTenant = tenantRepository.findById(TenantContext.getTenantId()).orElseThrow(() -> new EntityNotFoundException("Tenant No Encontrado"));

        Container newContainer = containerMapper.toContainerEntity(containerData);
        newContainer.setTenant(userTenant);
        newContainer.setSector(sector);

        Container storedContainer = containerRepository.save(newContainer);
        return containerMapper.toSimpleDTO(storedContainer);

    }

    private void changeContainerSector(Container container, Long sectorId) {
        Sector destinationSector = sectorRepository.getSectorById(sectorId).orElseThrow(() -> new EntityNotFoundException("Sector Not Found"));
        container.setSector(destinationSector);
    }

    @Transactional
    public ImageDTO addImageToContainer(long containerId, MultipartFile imageMPF) {
        Container container = containerRepository.getContainerById(containerId).orElseThrow(() -> new EntityNotFoundException("Container Not Found"));

        Image image = new Image();
        image.setContainer(container);
        return imageService.addImageToContainer(imageMPF, containerId, "container-images");
    }

    @Transactional
    public void deleteImageFromContainer(long imageId, long containerId) {
        imageService.deleteImageFromContainer(imageId, containerId);
    }

    public List<ImageDTO> getAllImagesFromContainer(long itemId) {
        return imageService.getAllImagesByContainerId(itemId);
    }

    @Transactional
    public FullContainerDTO getContainerByTagId(String tagId) {
        Container existingContainer = containerRepository.findByRfidTag(tagId);
        return containerMapper.toFullContainerDTO(existingContainer);
    }
}
