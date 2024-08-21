package com.example.FileUploadAndFileFormat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public interface ImageService {

    String uploadImage(MultipartFile file) throws IOException;
    String uploadCompressedImage(MultipartFile file) throws IOException;


    byte[] downloadImage(Integer imageId) throws UnsupportedEncodingException;
}
