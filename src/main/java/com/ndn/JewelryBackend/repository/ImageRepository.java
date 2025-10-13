package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
