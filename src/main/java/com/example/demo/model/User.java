package com.example.demo.model;

import com.example.demo.types.UserRoleType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRoleType role;
    private boolean isDisabled;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_items",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> favoriteItems;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @PrePersist
    public void OnCreate() {
        this.favoriteItems = new HashSet<>();
        this.role = UserRoleType.USER;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Item> getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(Set<Item> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

    public UserRoleType getRole() {
        return role;
    }

    public void setRole(UserRoleType role) {
        this.role = role;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isDisabled == user.isDisabled && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email) && role == user.role && Objects.equals(favoriteItems, user.favoriteItems) && Objects.equals(tenant, user.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, name, surname, email, role, isDisabled, favoriteItems, tenant);
    }
}
