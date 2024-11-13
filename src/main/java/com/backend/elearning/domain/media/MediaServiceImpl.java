package com.backend.elearning.domain.media;

import com.backend.elearning.exception.BadRequestException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MediaServiceImpl implements MediaService {

    private final Cloudinary cloudinary;

    private final RestTemplate restTemplate;

    public MediaServiceImpl(Cloudinary cloudinary, RestTemplate restTemplate) {
        this.cloudinary = cloudinary;
        this.restTemplate = restTemplate;
    }


    @Override
    public Media saveOrUpdateFile(MultipartFile multipartFile, String uuid, String type) {
        try {
            HashMap<String, String> map = new HashMap<>();
            String fileId = "";
            if (!uuid.isEmpty()) {
                fileId = uuid;
            } else {
                fileId = UUID.randomUUID().toString();
            }
            map.put("public_id", fileId);
            map.put("resource_type", "auto");
            Map uploadResult = cloudinary.uploader()
                    .upload(multipartFile.getBytes(), map);
            String url = uploadResult
                    .get("secure_url")
                    .toString();
            Media media =  Media.builder()
                    .id(fileId)
                    .url(url)
                    .fileName(multipartFile.getOriginalFilename())
                    .build();
            if (type.equals("video")) {
                String duration = uploadResult.get("duration").toString();
                media.setDuration(duration);
            }
            return media;
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public String getUrlById(String uuid) {
        return cloudinary.url() // Add any transformations if needed
                .generate(uuid);
    }

    @Override
    public void deleteFile(String uuid) {
        try {
            cloudinary.uploader().destroy(uuid, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String fileUrl) throws IOException {
        return restTemplate.getForObject(fileUrl, byte[].class);
    }


}
