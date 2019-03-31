package com.MG.ManagingSystem.models.forms;

import com.MG.ManagingSystem.models.entities.GroupEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserForm {
    private int userId;
    @Pattern(regexp = "[A-Za-z]+[\\w]+")
    @Size(min = 3, max = 30)
    @NotBlank
    @NotEmpty
    private String userName;
    @Pattern(regexp = "[A-Za-z]+")
    @Size(min = 2, max = 30)
    @NotBlank
    @NotEmpty
    private String lastName;
    @Pattern(regexp = "[A-Za-z]+")
    @Size(min = 2, max = 30)
    @NotBlank
    @NotEmpty
    private String firstName;
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String password;
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String repeatedPassword;
    private Set<GroupEntity> groupsName;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}")
    private String stringDate;
    private LocalDate dateOfBirth;

    public UserForm(int userId,String userName, String lastName, String firstName, String password, Set<GroupEntity> groupsName,LocalDate dateOfBirth, String stringDate) {
        this.userId = userId;
        this.userName = userName;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.groupsName = groupsName;
        this.dateOfBirth = dateOfBirth;
        this.stringDate = stringDate;
    }
}
