package com.davidauz.blkm_interface.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="nations")
public class Nation
{
    private static final long serialVersionUID = 1L;

    // COLUMNS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nationId;

    @Column(nullable=false, unique=true)
    private String nation;
}
