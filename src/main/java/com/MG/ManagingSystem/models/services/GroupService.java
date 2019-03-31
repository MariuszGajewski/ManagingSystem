package com.MG.ManagingSystem.models.services;

import com.MG.ManagingSystem.models.entities.GroupEntity;
import com.MG.ManagingSystem.models.entities.UserEntity;
import com.MG.ManagingSystem.models.forms.UserListForm;
import com.MG.ManagingSystem.models.repositiories.GroupRepository;
import com.MG.ManagingSystem.models.repositiories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupService {
    public enum groupOperationStatus {
        OK, BUSY_NAME, ERROR;
    }

    final
    GroupRepository groupRepository;
    final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public groupOperationStatus addGroup(String groupName){
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupName);
        if(groupRepository.existsByName(groupName)){
            return groupOperationStatus.BUSY_NAME;
        }
        groupEntity = groupRepository.save(groupEntity);
        if(groupEntity != null){
            return groupOperationStatus.OK;
        }
        return groupOperationStatus.ERROR;
    }

    public void deleteGroup(String groupName){
        GroupEntity groupEntity = groupRepository.findByName(groupName);
        Set<UserEntity> UsersSet = userRepository.findByGroupsName(groupName);
        for (UserEntity userEntity : UsersSet) {
            userEntity.getGroups().remove(groupEntity);
            userRepository.save(userEntity);
        }
        groupRepository.delete(groupEntity);
    }

    public Page<GroupEntity> getGroupList(int pageIndex){
        return groupRepository.findAllByOrderByIdAsc(new PageRequest(pageIndex,5));
    }

    public List<Integer> userInGroupChecker(Page<UserEntity> page, String groupName){
        Set<UserEntity> usersInGroup = userRepository.findByGroupsName(groupName);
        List<Integer> userList = new ArrayList<>();
        for (UserEntity userEntity : page.getContent()) {
            if(usersInGroup.contains(userEntity)){
                userList.add(userEntity.getId());
            }
        }
        return userList;
    }

    public void updateGroup(UserListForm userListForm){
        GroupEntity groupEntity = groupRepository.findByName(userListForm.getGroupName());
        System.out.println("NAZWA GRUPY:::: " + groupEntity.getName());
        List<UserEntity> oldUsersSet = userRepository.findByGroupsName(userListForm.getGroupName(),
                new PageRequest(userListForm.getNumberOfVisibleListPage(),5)).getContent();
        Set<UserEntity> newUsersSet = new HashSet<>();
        for (Integer integer : userListForm.getListOfUsers()) {
            newUsersSet.add(userRepository.findOne(integer));
        }
        for (UserEntity userEntity : oldUsersSet) {
            userEntity.getGroups().remove(groupEntity);
            userRepository.save(userEntity);
        }
        for (UserEntity userEntity : newUsersSet) {
            userEntity.getGroups().add(groupEntity);
            userRepository.save(userEntity);
        }
    }
}
