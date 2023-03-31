package com.davidauz.blkm_interface.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="groups")
public class Group
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable=false, unique=true)
    @Size(min=2)
    @NotNull
    private String groupName;

    @Column(nullable=false, unique=true)
    @Size(min=2)
    @NotNull
    private String groupDesc;

    @ManyToMany
    @JoinTable(
            name="persons_groups",
            inverseJoinColumns={@JoinColumn(name="PERSON_ID", referencedColumnName="personId")},
            joinColumns={@JoinColumn(name="GROUP_ID", referencedColumnName="groupId")}
    )
    private List<Person> people;

    @Column(nullable=false)
    private boolean active;
}
