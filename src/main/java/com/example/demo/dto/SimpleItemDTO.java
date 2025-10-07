package com.example.demo.dto;
public class SimpleItemDTO {

    private Long id;

    private String tagId;

    private String name;

    private String description;

    private String locationDescription;

    private Long containerId;

    private String usedBy;

    public SimpleItemDTO(Long id, String tagId, String name, String description, String locationDescription, Long containerId, String usedBy) {
        this.id = id;
        this.tagId = tagId;
        this.name = name;
        this.description = description;
        this.locationDescription = locationDescription;
        this.containerId = containerId;
        this.usedBy = usedBy;
    }

    public SimpleItemDTO(Builder builder){
        this.id = builder.id;
        this.tagId = builder.tagId;
        this.name = builder.name;
        this.description = builder.description;
        this.locationDescription = builder.locationDescription;
        this.containerId = builder.containerId;
        this.usedBy = builder.usedBy;
    }

    public static class Builder{
        private Long id;
        private String tagId;
        private String name;
        private String description;
        private String locationDescription;
        private Long containerId;
        private String usedBy;
        public Builder id(Long id){
            this.id = id;
            return this;
        }
        public Builder tagId (String tagId){
            this.tagId = tagId;
            return this;
        }
        public Builder name (String name){
            this.name = name;
            return this;
        }
        public Builder description(String description){
            this.description = description;
            return this;
        }
        public Builder locationDescription(String locationDescription){
            this.locationDescription = locationDescription;
            return this;
        }
        public Builder containerId(Long containerId){
            this.containerId = containerId;
            return this;
        }
        public Builder usedBy(String username){
            this.usedBy = username;
            return this;
        }
        public SimpleItemDTO build(){
           return new SimpleItemDTO(this);
        }

    }

    public SimpleItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }
}
