package com.example.banvemaybay.controllers;

import com.example.banvemaybay.models.Post;
import com.example.banvemaybay.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix:/api/v1}/posts")
public class PostController {
    @Autowired
    private PostService postService;

    // Cập nhật đường dẫn lưu ảnh vào thư mục mới
    private final String uploadDir = "C:/D/WebsiteBanVeMayBay/banvemaybay/src/public/images/";

    // API lấy danh sách bài viết
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

        // Kiểm tra và lưu ảnh nếu có
        if (image != null && !image.isEmpty()) {
            String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            String filePath = Paths.get(uploadDir, imageName).toString();
            File uploadFolder = new File(uploadDir);

            // Nếu thư mục chưa tồn tại thì tạo mới
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            image.transferTo(new File(filePath));

            post.setImageUrl(imageName);
        }

        return postService.savePost(post);
    }

}
