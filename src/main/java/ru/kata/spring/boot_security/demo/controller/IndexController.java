package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

	private final UserService service;
	private final RoleService roleService;

	public IndexController(UserService service, RoleService roleService) {
		this.service = service;
		this.roleService = roleService;
	}

	@GetMapping(value = "/")
	public String printWelcome() {
		return "index";
	}
	
	@GetMapping(value = "/admin")
	public String printUsers(ModelMap model) {
		List<User> users = service.getUserList();
		model.addAttribute("users", users);
		return "admin";
	}
	
	@GetMapping(value = "/user")
	public String printUser(ModelMap model, Authentication auth) {
		User user;
		if (auth != null) {
			user = service.getUserByName(auth.getName());
			model.addAttribute("user", user);
			return "user";
		}
		return "index";
	}
	
	@GetMapping(value = "/userinfo", params = {"newuser"})
	public String addUser(ModelMap model) {
		User user = new User();
		List<Role> roles = new ArrayList<>();
		user.setRoles(roles);
		roles.add(roleService.getRoleByName("ADMIN"));
		roles.add(roleService.getRoleByName("USER"));
		model.addAttribute("user", user);
		model.addAttribute("selectableRoles", roles);
		model.addAttribute("action", "new");
		return "userinfo";
	}

	@GetMapping(value = "/admin/{id}")
	public String updateUser(ModelMap model, @PathVariable Long id) {
		User user = service.getUserById(id);
		List<Role> roles = new ArrayList<>();
		roles.add(roleService.getRoleByName("ADMIN"));
		roles.add(roleService.getRoleByName("USER"));
		model.addAttribute("user", user);
		model.addAttribute("selectableRoles", roles);
		model.addAttribute("action", "update");
		return "userinfo";
	}

	@PostMapping(value = "/userinfo")
	public String saveUserInfo(@ModelAttribute("user") User user) {
		service.addUser(user);
		return "redirect:/admin";
	}

	@PatchMapping(value = "/userinfo", params = {"update"})
	public String updateUserInfo(@ModelAttribute User user) {
		service.updateUser(user);
		return "redirect:/admin";
	}

	@DeleteMapping(value = "/admin/{id}")
	public String deleteUser(@PathVariable Long id) {
		service.deleteUser(id);
		return "redirect:/admin";
	}

}