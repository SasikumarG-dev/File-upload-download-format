package com.example.FileUploadAndFileFormat.service.impl;

import com.example.FileUploadAndFileFormat.entity.Image;
import com.example.FileUploadAndFileFormat.repository.ImageRepository;
import com.example.FileUploadAndFileFormat.service.ImageService;
import com.example.FileUploadAndFileFormat.uitl.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .build();
        log.info("file size {}",file.getSize());
        imageRepository.save(image);
        return "ImageUtils uploaded successfully";
    }

    public String uploadCompressedImage(MultipartFile file) {
        try {

            log.info(" size {}",file.getSize());

            // Compress the image with a desired quality (e.g., 0.5)
            byte[] compressedImageData = compressImageSize(file);

            // Create and save the Image entity
            Image image = Image.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .data(compressedImageData)
                    .build();
            log.info("compressed size {}",compressedImageData.length);
            imageRepository.save(image);
            return "Image uploaded and compressed successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading image";
        }
    }

    public byte[] downloadImage(Integer imageId) throws UnsupportedEncodingException {

        Optional<Image> dbImageData = imageRepository.findById(imageId);
        //byte[] images = ImageUtils.decompressImage(dbImageData.get().getData());
        return dbImageData.get().getData();
    }


    private byte[] compressImageSize(MultipartFile image) throws IOException {
        InputStream inputStream = image.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        float imageQuality = 0.1f;

        // Create the buffered image
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        // Get image writers
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg"); // Input your Format Name here

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = imageWriters.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        // Set the compress quality metrics
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);

        // Compress and insert the image into the byte array.
        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

        byte[] imageBytes = outputStream.toByteArray();

        // close all streams
        inputStream.close();
        outputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();


        return imageBytes;
    }

}