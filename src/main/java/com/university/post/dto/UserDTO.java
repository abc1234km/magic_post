package com.university.post.dto;

import com.university.post.enums.Type;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.Role;
import com.university.post.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO extends AuditDTO<String> {
    private String id;
    private Integer userId;

    private String fullName;
    private LocalDate dateOfBirth;
    private String phoneNo;
    private String address;
    private String email;
    private Role role;

    private DeliveryPointDTO workAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.fullName = user.getFullName();
        this.dateOfBirth = user.getDateOfBirth();
        this.phoneNo = user.getPhoneNo();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.workAt = user.getWorkAt() != null ? new DeliveryPointDTO(user.getWorkAt()) : null;
        this.createBy = user.getCreateBy();
        this.createAt = user.getCreateAt();
        this.updateBy = user.getUpdateBy();
        this.updateAt = user.getUpdateAt();
    }
}
