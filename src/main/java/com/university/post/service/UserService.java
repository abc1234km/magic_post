package com.university.post.service;

import com.university.post.dataset.UserDS;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import com.university.post.payload.request.user.UserRequest;
import org.springframework.data.domain.Page;

import java.util.*;

public interface UserService {
    public User addUser(UserRequest userRequest);

    public User updateUser(String id, UserRequest userRequest);

    public void deleteUser(User user);

    public User getUserById(String userId);

    public Page<User> getAllUser(UserDS userDS);

    public User handleUserRegister(UserRequest userRequest);

    public Boolean checkValidUser(UserRequest userRequest);

    public DeliveryPoint getWorkAtUser();

    public void changePasswordUser(String newPassword);
}
