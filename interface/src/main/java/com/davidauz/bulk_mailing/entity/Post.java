package com.davidauz.bulk_mailing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="posts")
public class Post {
    private static final long serialVersionUID = 1L;

    // COLUMNS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    @Size(min=2, max=100)
    private String title;

    @Column(nullable=false, columnDefinition="TEXT")
    @Size(min=2)
    private String content;
}
