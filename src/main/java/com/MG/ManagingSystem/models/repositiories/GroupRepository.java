package com.MG.ManagingSystem.models.repositiories;

import com.MG.ManagingSystem.models.entities.GroupEntity;
import com.MG.ManagingSystem.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<GroupEntity,Integer> {
        GroupEntity findById(int id);
        Page<GroupEntity> findAllByOrderByIdAsc(Pageable pageable);
        GroupEntity findByName(String groupName);
        boolean existsByName(String groupName);
}
