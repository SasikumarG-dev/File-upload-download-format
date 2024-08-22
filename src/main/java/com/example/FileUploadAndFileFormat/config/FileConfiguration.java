package com.example.FileUploadAndFileFormat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:file-configuration.properties")
@ConfigurationProperties(prefix = "file")
@Data
public class FileConfiguration {

    private float imageQualityLevel;
}