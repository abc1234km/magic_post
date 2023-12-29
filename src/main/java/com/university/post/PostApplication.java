package com.university.post;

import com.university.post.model.Role;
import com.university.post.model.User;
import com.university.post.repository.UserRepository;
import com.university.post.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class PostApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PostApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
    RoleService roleService;

	@Override
	public void run(String... args) throws Exception {
//		System.out.println(passwordEncoder.encode("20020001"));
//		System.out.println(passwordEncoder.encode("20020002"));
//		System.out.println(passwordEncoder.encode("20020003"));
		// Khi chương trình chạy
		// Insert vào csdl một user.
//		User user = new User();
//		user.setUsername("loda");
//		user.setPassword(passwordEncoder.encode("loda"));
//		userRepository.save(user);
//		System.out.println(userRepository.findAll());
//		Role role1 = new Role();
//		role1.setRole(com.university.post.enums.Permission.COMPANY_ADMIN);
//		Role role2 = new Role();
//		role2.setRole(com.university.post.enums.Permission.TRANSACTION_ADMIN);
//		Role role3 = new Role();
//		role3.setRole(com.university.post.enums.Permission.WAREHOUSE_STAFF);
//		Role role4 = new Role();
//		role4.setRole(com.university.post.enums.Permission.CUSTOMER);
//
//		roleService.addRole(role1);
//		roleService.addRole(role2);
//		roleService.addRole(role3);
//		roleService.addRole(role4);
//
//		User user = new User();
//		user.setUserId(123456);
//		user.setPassword(passwordEncoder.encode("admin"));
//		user.setRole(role1);
//		userRepository.save(user);
	}
}
