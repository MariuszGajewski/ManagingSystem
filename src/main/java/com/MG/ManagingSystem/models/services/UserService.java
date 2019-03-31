package com.MG.ManagingSystem.models.services;

import com.MG.ManagingSystem.models.Utils;
import com.MG.ManagingSystem.models.entities.GroupEntity;
import com.MG.ManagingSystem.models.entities.UserEntity;
import com.MG.ManagingSystem.models.forms.UserForm;
import com.MG.ManagingSystem.models.repositiories.GroupRepository;
import com.MG.ManagingSystem.models.repositiories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    public enum userOperationStatus {
        OK, BUSY_LOGIN, ERROR, INCORRECT_LOGIN_DATA;
    }
    final
    UserRepository userRepository;
    final
    GroupRepository groupRepository;

    @Autowired
    public UserService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }
    public Page<UserEntity> getUserList(int pageIndex){
        return userRepository.findAllByOrderByIdAsc(new PageRequest(pageIndex,5));
    }
    public Page<UserEntity> getUserByGroup(int pageIndex, String groupName) {
        return userRepository.findByGroupsName(groupName,new PageRequest(pageIndex,5));
    }
    public userOperationStatus addUser(UserForm userForm){
        if(busyLoginCheck(userForm.getUserName()) && userForm.getUserId() == 0) {
            return userOperationStatus.BUSY_LOGIN;
        }
        Utils utils= new Utils();
        UserEntity userEntity = new UserEntity();
        if(userForm.getUserId() > 0){
            userEntity = userRepository.findOne(userForm.getUserId());
        }
        userEntity.setDateOfBirth(userForm.getDateOfBirth());
        userEntity.setFirstName(userForm.getFirstName());
        userEntity.setLastName(userForm.getLastName());
        if(!userForm.getPassword().isEmpty()) {
            userEntity.setPassword(
                    utils.hashPassword(userForm.getPassword())
            );
        }
        userEntity.setUserName(userForm.getUserName());
        Set<GroupEntity> groupSet = new HashSet<>();
        for (GroupEntity group : userForm.getGroupsName()) {
            groupSet.add(groupRepository.findById(group.getId()));
        }
        userEntity.setGroups(groupSet);
        userEntity=userRepository.save(userEntity);
        if(userEntity == null) {
            return  userOperationStatus.ERROR;
        }
        return userOperationStatus.OK;
    }

    private boolean busyLoginCheck(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public void deleteUser(int userId){
        UserEntity userEntity=userRepository.findOne(userId);
        Set<GroupEntity> groups = userEntity.getGroups();
        groups.clear();
        userEntity.setGroups(groups);
        userRepository.save(userEntity);
        userRepository.delete(userEntity);
    }
}
