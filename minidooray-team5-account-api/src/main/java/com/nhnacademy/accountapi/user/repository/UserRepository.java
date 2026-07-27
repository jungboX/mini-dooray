package com.nhnacademy.accountapi.user.repository;

import com.nhnacademy.accountapi.user.domain.User;
import com.nhnacademy.accountapi.user.dto.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.status != 'TERMINATE'")
    List<UserResponse> findAllBy();

    @Modifying
    @Query("UPDATE User u SET u.status = 'TERMINATE' WHERE u.username = :username")
    void leaveUser(String username);
}
