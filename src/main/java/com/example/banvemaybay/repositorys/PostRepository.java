package com.example.banvemaybay.repositorys;

import com.example.banvemaybay.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    void deleteById(Integer id);
}