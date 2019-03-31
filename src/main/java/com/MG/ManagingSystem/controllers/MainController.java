package com.MG.ManagingSystem.controllers;

import com.MG.ManagingSystem.models.entities.GroupEntity;
import com.MG.ManagingSystem.models.entities.UserEntity;
import com.MG.ManagingSystem.models.forms.UserForm;
import com.MG.ManagingSystem.models.forms.UserListForm;
import com.MG.ManagingSystem.models.repositiories.GroupRepository;
import com.MG.ManagingSystem.models.repositiories.UserRepository;
import com.MG.ManagingSystem.models.services.GroupService;
import com.MG.ManagingSystem.models.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
public class MainController {

    final
    UserService userService;
    final GroupRepository groupRepository;
    final UserRepository userRepository;
    final GroupService groupService;

    @Autowired
    public MainController(UserService userService, GroupRepository groupRepository, UserRepository userRepository, GroupService groupService) {
        this.userService = userService;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupService = groupService;
    }
    @ModelAttribute
    public Model startingModel(Model model) {
        model.addAttribute("groupList",groupRepository.findAll());
        model.addAttribute("groupsCount",groupRepository.count());
        model.addAttribute("usersCount",userRepository.count());
        return model;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/loginPage")
    public String getLoginTemplate(){
        return "loginPage";
    }
    @GetMapping("/index")
    public String getIndex(){
        return "index";
    }

    @GetMapping("/userManager")
    public String getUsersList(Model model){

        model.addAttribute("selectedGroup","All");
        model.addAttribute("page", 1);
        model.addAttribute("userList",userService.getUserList(0));
        return "userManager";

    }
    @PostMapping("/userManager")
    public String getUsersByGroup(@RequestParam("groupSelector") String groupName, Model model) {
        Page<UserEntity> page= null;
        if(!groupName.equals("All")){
            page = userService.getUserByGroup(0,groupName);
        }else{
            page = userService.getUserList(0);
        }
        model.addAttribute("selectedGroup",groupName);
        model.addAttribute("page", 1);
        model.addAttribute("userList",page);
        return "userManager";
    }
    @GetMapping("/userManager/{pageNumber}/{groupName}")
    public String getUsersNextPage(@PathVariable("pageNumber") int pageNumber, @PathVariable("groupName") String groupName, Model model){
        Page<UserEntity> page= null;
        if(!groupName.equals("All")){
            page = userService.getUserByGroup(pageNumber-1,groupName);
        }else{
            page = userService.getUserList(pageNumber-1);
        }
        if(page.getTotalPages() == pageNumber ){
            model.addAttribute("nextPage",1);
        }
        model.addAttribute("selectedGroup",groupName);
        model.addAttribute("page", pageNumber);
        model.addAttribute("userList",page);
        return "userManager";
    }

    @GetMapping("/groupManager")
    public String getGroupTemplate(Model model){
        model.addAttribute("groupList",groupService.getGroupList(0));
        model.addAttribute("page", 1);
        return "groupManager";
    }

    @GetMapping("/groupManager/{pageNumber}")
    public String getGroupsNextPage(@PathVariable("pageNumber") int pageNumber, Model model){
        Page<GroupEntity> page = groupService.getGroupList(pageNumber-1);
        if(page.getTotalPages() == pageNumber ){
            model.addAttribute("nextPage",1);
        }
        model.addAttribute("page", pageNumber);
        model.addAttribute("groupList",page);
        return "groupManager";
    }

    @PostMapping("/showUserDetails")
    public String showUserDetails(@RequestParam("userId") int userId, Model model){
        UserEntity user = userRepository.findOne(userId);
        model.addAttribute("user", user);
        return "userDetails";
    }

    @PostMapping("/changeDetails")
    public String changeUserDetails(@RequestParam("userId") int userId, Model model){
        UserEntity user = userRepository.findOne(userId);
        user.getGroups().size();
        UserForm userForm = new UserForm(userId,user.getUserName(),user.getLastName(),user.getFirstName(),user.getPassword(), user.getGroups(),user.getDateOfBirth(), user.getDateOfBirth().toString());
        model.addAttribute("userForm", userForm);
        return "userForm";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId")int userId){
        userService.deleteUser(userId);
        return "redirect:/userManager";
    }

    @GetMapping("/addUser")
    public String getAddUserTemplate(Model model){
        model.addAttribute("userForm",new UserForm());
        return "userForm";
    }
    @PostMapping("/addUser")
    public String addNewUser(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            if(!(bindingResult.hasFieldErrors("password") && userForm.getUserId() >0 && userForm.getPassword().isEmpty())) {
                return "userForm";
            }
        }
        LocalDate date = LocalDate.parse(userForm.getStringDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        userForm.setDateOfBirth(date);
        UserService.userOperationStatus status = userService.addUser(userForm);
        if(status == UserService.userOperationStatus.BUSY_LOGIN){
            model.addAttribute("result", "Given user name is already in use");
            return "userForm";
        }
        if (status == UserService.userOperationStatus.ERROR){
            model.addAttribute("result","Ups... Something goes wrong");
            return "userForm";
        }
        return "redirect:/userManager";
    }

    @PostMapping("/changeGroup")
    public String changeGroupUsers(@RequestParam("groupSelector") String groupName, Model model){
        Page<UserEntity> page=userService.getUserList(0);
        List<Integer> userList= groupService.userInGroupChecker(page,groupName);
        UserListForm userListForm=new UserListForm();
        userListForm.setListOfUsers(userList);
        userListForm.setGroupName(groupName);
        userListForm.setNumberOfVisibleListPage(0);
        model.addAttribute("userListInGroup",userListForm);
        model.addAttribute("page", 1);
        model.addAttribute("userList",page);
        model.addAttribute("selectedGroup",groupName);
        return "groupUsers";
    }

    @GetMapping("/changeGroup/{pageNumber}/{groupName}")
    public String getGroupUsersNextPage(@PathVariable("pageNumber") int pageNumber, @PathVariable("groupName") String groupName, Model model){
        Page<UserEntity>page = userService.getUserList(pageNumber-1);
        List<Integer> userList= groupService.userInGroupChecker(page,groupName);
        UserListForm userListForm=new UserListForm();
        userListForm.setListOfUsers(userList);
        userListForm.setGroupName(groupName);
        userListForm.setNumberOfVisibleListPage(pageNumber-1);
        if(page.getTotalPages() == pageNumber ){
            model.addAttribute("nextPage",1);
        }
        model.addAttribute("userListInGroup",userListForm);
        model.addAttribute("selectedGroup",groupName);
        model.addAttribute("usersInGroup",userRepository.findByGroupsName(groupName));
        model.addAttribute("page", pageNumber);
        model.addAttribute("userList",page);
        return "groupUsers";
    }
    @PostMapping("/groupUpdate")
    public String updateGroup(@ModelAttribute("userListInGroup") UserListForm userListForm){
        groupService.updateGroup(userListForm);
        return "redirect:/groupManager";
    }

    @PostMapping("/addGroup")
    public String addGroup(@RequestParam("newGroupName")String newGroupName, RedirectAttributes redirectAttributes){
        GroupService.groupOperationStatus status =groupService.addGroup(newGroupName);
        if (status == GroupService.groupOperationStatus.BUSY_NAME){
            System.out.println("busy");
            redirectAttributes.addFlashAttribute("result","Given group name is already in use");
        }
        if (status == GroupService.groupOperationStatus.ERROR){
            redirectAttributes.addFlashAttribute("result","Ups... Something goes wrong");
        }
        return "redirect:/groupManager";
    }

    @PostMapping("deleteGroup")
    public String deleteGroup(@RequestParam("groupName") String groupName){
        System.out.println("ID grupy" + groupName);
        groupService.deleteGroup(groupName);
        return "redirect:/groupManager";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/loginPage?logout";
    }
}
