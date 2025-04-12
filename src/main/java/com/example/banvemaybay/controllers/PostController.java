package com.example.banvemaybay.controllers;

import com.example.banvemaybay.models.Post;
import com.example.banvemaybay.services.ImageUploadService;
import com.example.banvemaybay.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix:/api/v1}/posts")
public class PostController {

    private final PostService postService;
    private final ImageUploadService imageUploadService;

    public PostController(PostService postService, ImageUploadService imageUploadService) {
        this.postService = postService;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        if (image != null && !image.isEmpty()) {
            // Kiểm tra định dạng file
            String contentType = image.getContentType();
            if (!contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("File tải lên không phải là ảnh.");
            }
            String imageUrl = imageUploadService.uploadImage(image);
            post.setImageUrl(imageUrl);
        }

        Post savedPost = postService.savePost(post);
        return ResponseEntity.ok(savedPost);
    }

}
