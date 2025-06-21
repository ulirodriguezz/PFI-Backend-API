package com.example.demo.dto;

public class SimpleUserDTO {
    private String username;
    private String password;
    private String name;
    private String surname;

    private SimpleUserDTO(Builder builder){
        this.username = builder.username;
        this.password = builder.password;
        this.name = builder.name;
        this.surname = builder.surname;
    }

    public SimpleUserDTO() {
    }

    public static class Builder {
        private String username;
        private String password;
        private String name;
        private String surname;
        public Builder username(String username){
            this.username = username;
            return this;
        }
        public Builder password(String password){
            this.password = password;
            return this;
        }
        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder surname(String surname){
            this.surname = surname;
            return this;
        }

        public SimpleUserDTO build(){
            return new SimpleUserDTO(this);
        }


    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
