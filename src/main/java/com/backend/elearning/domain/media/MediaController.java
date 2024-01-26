package com.backend.elearning.domain.media;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/medias")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<Media> save (@RequestParam("file") MultipartFile multipartFile) {
        Media media = mediaService.saveOrUpdateFile(multipartFile, "");
        return ResponseEntity.ok().body(media);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Media> update (@RequestParam("file")MultipartFile multipartFile,
                                         @PathVariable("id") String id) {
        Media media = mediaService.saveOrUpdateFile(multipartFile, id);
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
