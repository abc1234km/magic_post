package com.university.post.payload.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String fullName;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

//    @Length(min=10)
    private String phoneNo;

    private String address;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    private Long role;

    private String workAt;
}
