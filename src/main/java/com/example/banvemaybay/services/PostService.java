package com.example.banvemaybay.services;

import com.example.banvemaybay.models.Post;
import com.example.banvemaybay.repositorys.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPost{
    private final PostRepository postRepository;

//    @Autowired
//    private PostRepository postRepository;

    @Override
    public void deleteByIdPost(Integer id) {
        Post post= postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id Post: " + Long.valueOf(id)));
        postRepository.deleteById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}
