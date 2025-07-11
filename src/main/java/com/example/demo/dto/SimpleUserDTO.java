package com.example.demo.dto;

public class SimpleUserDTO {

    private long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String token;

    private SimpleUserDTO(Builder builder){
        this.username = builder.username;
        this.id = builder.id;
        this.name = builder.name;
        this.surname = builder.surname;
        this.email = builder.email;
    }

    public SimpleUserDTO() {
    }

    public static class Builder {
        private long id;
        private String username;
        private String name;
        private String surname;
        private String email;
        private String token;
        public Builder username(String username){
            this.username = username;
            return this;
        }
        public Builder id(long id){
            this.id = id;
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
        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder token(String token){
            this.token = token;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
