package com.example.banvemaybay.controllers;

import com.example.banvemaybay.models.Post;
import com.example.banvemaybay.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private PostService postService;

    // Đường dẫn động: hoạt động cả khi local và Docker
    private final String uploadDir = System.getProperty("user.dir") + "/images/";

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public Post createPost(@RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        if (image != null && !image.isEmpty()) {
            // Tạo tên ảnh duy nhất
            String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            // Tạo thư mục nếu chưa tồn tại
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Lưu ảnh vào thư mục
            Path filePath = Paths.get(uploadDir, imageName);
            image.transferTo(filePath.toFile());

            post.setImageUrl(imageName); // chỉ lưu tên ảnh
        }

        return postService.savePost(post);
    }
}
