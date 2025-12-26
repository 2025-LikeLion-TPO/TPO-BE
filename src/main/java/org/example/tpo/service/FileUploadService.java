package org.example.tpo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadContactProfile(Long contactId, MultipartFile file);
}

