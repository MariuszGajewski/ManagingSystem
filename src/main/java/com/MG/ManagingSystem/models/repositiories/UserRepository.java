package com.MG.ManagingSystem.models.repositiories;

import com.MG.ManagingSystem.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface UserRepository extends CrudRepository<UserEntity,Integer> {
    Page<UserEntity> findAllByOrderByIdAsc(Pageable pageable);
    Page<UserEntity> findByGroupsName(String groupName,Pageable pageable);
    Set<UserEntity> findByGroupsName(String groupName);
    boolean existsByUserName(String userName);

}

