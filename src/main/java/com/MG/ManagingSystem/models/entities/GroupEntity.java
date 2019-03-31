package com.MG.ManagingSystem.models.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_group")
@Getter
@Setter
@NoArgsConstructor
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToMany( cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },mappedBy = "groups")
    private Set<UserEntity> users = new HashSet<>();
}
