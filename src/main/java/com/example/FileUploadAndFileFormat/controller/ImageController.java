package com.example.FileUploadAndFileFormat.controller;

import com.example.FileUploadAndFileFormat.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.uploadImage(file));
    }
    @PostMapping("/compress")
    public ResponseEntity<?> uploadCompressedImage(@RequestParam("image")MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.uploadCompressedImage(file));
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable Integer imageId) throws UnsupportedEncodingException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageService.downloadImage(imageId));
        //return ResponseEntity.status(HttpStatus.OK).body(imageService.downloadImage(imageId));
    }
}