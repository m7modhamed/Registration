package com.stickynotes.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findUserById(Long id);


    @Modifying
    @Query("update User u set u.firstName = :firstName ,u.lastName = :lastName , u.email = :email    where u.id = :id")
    void updateUser(@Param(value = "id") long id, @Param(value = "firstName") String firstName ,@Param(value = "lastName") String lastName,@Param(value = "email") String email);

}
