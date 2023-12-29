package com.university.post.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDS extends Paging{
    private Integer userId;

    private String fullName;

    private String phoneNo;

    private String address;

    private String email;

    private Long role;

    private Boolean hasWorkAt;

    private Long workAt;

    private String typePoint;
}
