package com.university.post.service.impl;

import com.university.post.exception.role.RoleNotFound;
import com.university.post.model.Role;
import com.university.post.repository.RoleRepository;
import com.university.post.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFound("Không tìm thấy role với id " + id));
    }

    public void addRole(Role role) {
        roleRepository.save(role);
    }
}
