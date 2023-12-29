package com.university.post.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name= "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="roles_id_seq")
    @SequenceGenerator(name="roles_id_seq", sequenceName="roles_id_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
}
