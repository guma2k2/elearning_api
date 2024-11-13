package com.backend.elearning.domain.media;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaService {
    Media saveOrUpdateFile (MultipartFile multipartFile, String uuid, String type);
    String getUrlById (String uuid);
    void deleteFile (String uuid);
    byte[] downloadFile(String publicId) throws IOException;
}
