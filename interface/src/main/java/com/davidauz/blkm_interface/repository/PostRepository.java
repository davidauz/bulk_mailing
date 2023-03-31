package com.davidauz.blkm_interface.repository;

import com.davidauz.blkm_interface.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable paging);
    Optional<Post> findById(long Id);

    List<IdTitle> findAllBy();
}


