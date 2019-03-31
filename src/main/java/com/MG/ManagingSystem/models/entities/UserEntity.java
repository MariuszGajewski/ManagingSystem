package com.MG.ManagingSystem.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "group_of_user",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_group_id") }
    )
    Set<GroupEntity> groups = new HashSet<>();


    public Date getDateAsJavaUtil(){
        return Date.from(dateOfBirth.atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
