package com.example.banvemaybay.controllers.admin;

import com.example.banvemaybay.dtos.NguoiDatDTO;
import com.example.banvemaybay.dtos.ThongTinDatVeChiTietDTO;
import com.example.banvemaybay.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/post")
@RequiredArgsConstructor
public class PostAdminController {

    private final PostService postService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostWidthId(@PathVariable("id") Integer id){
        try{
            postService.deleteByIdPost(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
