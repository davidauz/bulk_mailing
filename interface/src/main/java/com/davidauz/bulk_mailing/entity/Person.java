package com.davidauz.bulk_mailing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="persons")
public class Person
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @Column(nullable=false, unique=true)
    @Size(min=2, max=30)
    @NotNull
    private String firstName;

    @Column(nullable=false, unique=true)
    @Size(min=2, max=30)
    @NotNull
    private String familyName;

    @OneToOne()
    @JoinColumn(name="company", referencedColumnName="id")
    private Company company;

    @ManyToMany()
    @JoinTable(
        name="persons_groups",
        joinColumns={@JoinColumn(name="PERSON_ID", referencedColumnName="personId")},
        inverseJoinColumns={@JoinColumn(name="GROUP_ID", referencedColumnName="groupId")}
    )
    private List<Group> groups;

    @Column
    @Nullable
    @Pattern(regexp = "^$|^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message="Email address is invalid")
    private String email;

    @Column(nullable=false)
    private boolean active;
}
