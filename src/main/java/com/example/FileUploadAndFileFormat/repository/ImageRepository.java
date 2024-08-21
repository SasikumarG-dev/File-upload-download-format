package com.example.FileUploadAndFileFormat.repository;

import com.example.FileUploadAndFileFormat.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    Optional<Image> findByName(String fileName);
}