package com.davidauz.bulk_mailing.repository;

import com.davidauz.bulk_mailing.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable paging);
}


