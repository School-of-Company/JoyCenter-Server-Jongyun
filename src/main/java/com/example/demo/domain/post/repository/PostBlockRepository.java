package com.example.demo.domain.post.repository;

import com.example.demo.domain.post.entity.PostBlockEntity;
import com.example.demo.domain.post.entity.PostEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostBlockRepository extends JpaRepository<PostBlockEntity,Long> {

    @Query("SELECT pb FROM PostBlockEntity pb " +
            "LEFT JOIN FETCH pb.attachments " +
            "WHERE pb.post = :post " +
            "ORDER BY pb.blockOrder ASC")
    List<PostBlockEntity> findAllByPostWithAttachments(@Param("post") PostEntity post);

    void deleteAllByPostAndIdNotIn(PostEntity post, List<Long> ids);
    List<PostBlockEntity> findAllByPostIdOrderByBlockOrderAsc(Long postId);
    void deleteAllByPost(PostEntity post);
}
