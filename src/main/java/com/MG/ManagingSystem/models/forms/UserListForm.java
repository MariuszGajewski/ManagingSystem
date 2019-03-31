package com.MG.ManagingSystem.models.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserListForm {
    private String groupName;
    private List<Integer> listOfUsers;
    private int numberOfVisibleListPage;
}
