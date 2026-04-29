package com.ticketing.tenant.ui.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.service.UserService;
import com.ticketing.tenant.ui.dto.requests.UserRequestDTO;
import com.ticketing.tenant.ui.dto.responses.UserResponseDTO;
import com.ticketing.tenant.utils.AppConstants;

import jakarta.validation.Valid;


@RestController
@RequestMapping(AppConstants.BASE_URL + AppConstants.USER_API)
public class UserController {
	
	  @Autowired
	    private UserService userService;

	    @GetMapping
	    public List<User> getUsers(@RequestParam Long tenantId) {
	        return userService.getUsersByTenant(tenantId);
	    }
	    
	    
	    @PostMapping
	    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO user) {
	        return userService.createUser(user);
	    }

}
