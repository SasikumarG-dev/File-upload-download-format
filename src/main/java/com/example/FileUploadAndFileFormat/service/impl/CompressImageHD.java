package com.example.FileUploadAndFileFormat.service.impl;

import com.example.FileUploadAndFileFormat.config.FileConfiguration;
import com.example.FileUploadAndFileFormat.entity.Image;
import com.example.FileUploadAndFileFormat.repository.ImageRepository;
import com.example.FileUploadAndFileFormat.service.ImageService;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
@Qualifier("compressLessSize")
public class CompressImageHD implements ImageService {


    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private FileConfiguration fileConfiguration;


    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        return "";
    }

    @Override
    public String uploadCompressedImage(MultipartFile file) throws IOException {
        log.info("HD ");
        log.info(" size {}",file.getSize());

        // Compress the image with a desired quality (e.g., 0.5)
        byte[] compressedImageData =compressImage(file, fileConfiguration.getImageQualityLevel());

        // Create and save the Image entity
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(compressedImageData)
                .build();
        log.info("compressed size {}",compressedImageData.length);
        imageRepository.save(image);
        return "Image uploaded and compressed successfully!";
    }

    @Override
    public byte[] downloadImage(Integer imageId) throws UnsupportedEncodingException {
        Optional<Image> dbImageData = imageRepository.findById(imageId);
        //byte[] images = ImageUtils.decompressImage(dbImageData.get().getData());
        return dbImageData.get().getData();
    }

    public byte[] compressImage(MultipartFile image, double quality) throws IOException {
        if (quality < 0 || quality > 1) {
            throw new IllegalArgumentException("Quality must be between 0 and 1");
        }

        InputStream inputStream = image.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        float imageQuality = fileConfiguration.getImageQualityLevel();

        // Create the buffered image
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        Thumbnails.of(bufferedImage)
                .size(400, 600)
                .outputFormat("JPEG")
                .outputQuality(quality)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        return data;
    }
}