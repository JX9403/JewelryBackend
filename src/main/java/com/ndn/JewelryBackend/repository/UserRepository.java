package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Product;
import com.ndn.JewelryBackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findByEmailContainingIgnoreCaseOrderByCreatedAtDesc(String email, Pageable pageable);

}
