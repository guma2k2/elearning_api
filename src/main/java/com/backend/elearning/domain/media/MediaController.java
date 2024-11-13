package com.backend.elearning.domain.media;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/medias")
@Slf4j
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<Media> save (@RequestParam("file") MultipartFile multipartFile,
                                       @RequestParam("type") String type) {
        Media media = mediaService.saveOrUpdateFile(multipartFile, "", type);
        return ResponseEntity.ok().body(media);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String fileUrl, @RequestParam String fileName) {
        try {
            byte[] fileBytes = mediaService.downloadFile(fileUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.set(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Media> update (@RequestParam("file")MultipartFile multipartFile,
                                         @PathVariable("id") String id,
                                         @PathVariable("type") String type) {
        Media media = mediaService.saveOrUpdateFile(multipartFile, id, type);
        return ResponseEntity.ok().body(media);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> get (@PathVariable("id") String id) {
        String url = mediaService.getUrlById(id);
        return ResponseEntity.ok().body(url);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") String id) {
        mediaService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}
