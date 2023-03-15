package com.davidauz.bulk_mailing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name="projects")
public class Project
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    @Size(min=2)
    @NotNull
    private String mailSubject;

    @Column
    @Size(min=0, max=30)
    @Nullable
    private String description;

    @Column
    @NotNull
    private Long postId;

    @ManyToMany
    @JoinTable(
            name="projects_people",
            inverseJoinColumns={@JoinColumn(name="PERSON_ID", referencedColumnName="personId")},
            joinColumns={@JoinColumn(name="PROJECT_ID", referencedColumnName="Id")}
    )
    private List<Person> people;


    @ManyToMany
    @JoinTable(
            name="projects_companies",
            inverseJoinColumns={@JoinColumn(name="COMPANY_ID", referencedColumnName="Id")},
            joinColumns={@JoinColumn(name="PROJECT_ID", referencedColumnName="Id")}
    )
    private List<Company> companies;


    @ManyToMany
    @JoinTable(
            name="projects_groups",
            inverseJoinColumns={@JoinColumn(name="GROUP_ID", referencedColumnName="groupId")},
            joinColumns={@JoinColumn(name="PROJECT_ID", referencedColumnName="Id")}
    )
    private List<Group> groups;

    @Column(nullable=false)
    private boolean active;
}
