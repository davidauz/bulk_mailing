package com.davidauz.blkm_interface.entity;

import com.davidauz.blkm_interface.util.TimeUtil;
import com.davidauz.blkm_interface.util.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.ORDINAL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column
    private Timestamp tokenValidation;

    @Column(nullable = false)
    @Enumerated(ORDINAL) // JPA enum persistence
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable
    (   name="users_roles"
    ,        joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")}
    ,        inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")}
    )
    private List<Role> roles = new ArrayList<>();

    public boolean validated() {
        return tokenValidation != null;
    }

    public void markTokenAsValid() {
        TimeUtil timeu = new TimeUtil();
        tokenValidation=timeu.now();
    }
}
