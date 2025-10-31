package com.example.demo.repository;

import com.example.demo.model.Item;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> getUserById(long id);
    @Query("SELECT u.favoriteItems FROM User u WHERE u.username = :username")
    Set<Item> getUserFavoriteItems(@Param("username") String username);

    @Query("SELECT i.id FROM User u JOIN u.favoriteItems i WHERE u.id = :userId")
    Set<Long> getUserFavoriteItemsIds(@Param("username") String username);

    @Query("FROM User u WHERE u.tenant.id = :tenantId ")
    List<User> findUsersByTenantId(@Param("tenantId") long tenantId);
    @Query("FROM User u WHERE u.isDisabled = :isDisabled")
    List<User> findAllByIsDisabled(@Param("isDisabled") boolean isDisabled);



}
