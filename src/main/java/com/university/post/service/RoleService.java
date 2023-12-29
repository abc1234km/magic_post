package com.university.post.service;

import com.university.post.exception.role.RoleNotFound;
import com.university.post.model.Role;
import com.university.post.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface RoleService {
    public Role getRoleById(Long id);

    public void addRole(Role role);
}
