package com.davidauz.blkm_common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="companies")
public class Company
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    @NotNull
    private String name;

    @Column
    @Nullable
    private String address;

    @Column
    @Nullable
    private String city;

    @ManyToOne // (cascade=CascadeType.ALL) this would cause deletion of the associated nation too
    @JoinColumn(name="nation_id")
    private Nation nation;


    @Column
    @Nullable
    @Pattern(regexp = "^$|^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message="Email address is invalid")
    private String email;

    @Column
    @Nullable
    @Pattern(regexp="^$|(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ;,./?%&=]*)?", message="Invalid web address")
    private String website;

    @Column(nullable=false)
    private boolean active;
}
