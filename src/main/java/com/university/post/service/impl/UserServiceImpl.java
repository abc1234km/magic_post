package com.university.post.service.impl;

import com.university.post.dataset.UserDS;
import com.university.post.enums.Permission;
import com.university.post.exception.entity.EntityNotFound;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.Role;
import com.university.post.model.User;
import com.university.post.payload.request.user.UserRequest;
import com.university.post.repository.UserRepository;
import com.university.post.security.CustomUserDetails;
import com.university.post.service.DeliveryPointService;
import com.university.post.service.RoleService;
import com.university.post.service.UserService;
import com.university.post.specification.UserSpecification;
import com.university.post.utils.Constant;
import com.university.post.utils.Utils;

import aj.org.objectweb.asm.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeliveryPointService deliveryPointService;

    @Override
    public User addUser(UserRequest userRequest) {
        User user = handleUserRegister(userRequest);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, UserRequest userRequest) {
        User user = getUserById(id);
        user.setFullName(userRequest.getFullName());
        user.setAddress(userRequest.getAddress());
        user.setEmail(userRequest.getEmail());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setPhoneNo(userRequest.getPhoneNo());

        DeliveryPoint workAt = user.getWorkAt();
        if (workAt == null) {
            Role role = roleService.getRoleById(userRequest.getRole());
            user.setRole(role);
        } 

        DeliveryPoint newDeliveryPoint = deliveryPointService.getDeliveryPointById(userRequest.getWorkAt());
        String typePoint = newDeliveryPoint.getTypeResource();
        if (typePoint.equals(com.university.post.enums.Type.TRANSACTION_POINT_LOCATION) && user.getRole().getId().equals(Permission.TRANSACTION_ADMIN)) {
            user.setWorkAt(newDeliveryPoint);
        } else if(typePoint.equals(com.university.post.enums.Type.GATHERING_POINT_LOCATION) && user.getRole().getId().equals(Permission.GATHERING_ADMIN)) {
            user.setWorkAt(newDeliveryPoint);
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFound("Không tìm thấy user với id " + id));
    }

    @Override
    public Page<User> getAllUser(UserDS userDS) {
        return userRepository.findAll(UserSpecification.getAllUserByConditions(userDS),
                PageRequest.of(userDS.getPageNumber() - 1, userDS.getPageSize(), Sort.by("createAt").descending()));
    }

    public User handleUserRegister(UserRequest userRequest) {
        Long nextId = userRepository.getNextId();
        User user = new User();
        user.setUserId((int) (Constant.STAFF_PREFIX_ID + nextId));
        user.setPassword(passwordEncoder.encode(String.valueOf(user.getUserId())));
        user.setFullName(userRequest.getFullName());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setAddress(userRequest.getAddress());
        user.setPhoneNo(userRequest.getPhoneNo());
        user.setEmail(userRequest.getEmail());
        Role role = roleService.getRoleById(userRequest.getRole());
        user.setRole(role);
        if (role.getId() != Permission.COMPANY_ADMIN) {
            user.setWorkAt(getWorkAtUser());
        }
        return user;
    }

    @Override
    public Boolean checkValidUser(UserRequest userRequest) {
        return Utils.isOver18YearsOld(userRequest.getDateOfBirth()) && Utils.isRoleValid(userRequest.getRole());
    }

    @Override
    public DeliveryPoint getWorkAtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUser().getWorkAt();
    }

    @Override
    public void changePasswordUser(String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
