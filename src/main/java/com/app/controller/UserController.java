package com.app.controller;

import com.app.entity.AuthRequest;
import com.app.entity.UserInfo;
import com.app.request.UserVo;
import com.app.service.JwtService;
import com.app.service.UserInfoDetails;
import com.app.service.UserInfoService;
import com.app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserVo userVo;
    
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }
    
    @GetMapping("/user/getDetails")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserVo> userDetails() {
        UserInfoDetails userDetails = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String username = userDetails.getUsername(); // Get the name

        return userService.getDetails(username); // This will now return a ResponseEntity
    }
    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    	
  	System.out.println(authRequest.getUsername()+""+authRequest.getPassword());
	
    	try {
    	    Authentication authentication = authenticationManager.authenticate(
    	        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
    	    );
    	    
    	    System.out.println("1");
    	    
    	    if (authentication.isAuthenticated()) {
    	        System.out.println("2");
    	        System.out.print(jwtService.generateToken(authRequest.getUsername()));
    	        return jwtService.generateToken(authRequest.getUsername());
    	    } else {
    	        System.out.println("Fail Else block");
    	        throw new UsernameNotFoundException("Invalid user request!");
    	    }
    	} catch (Exception e) {
    	    System.out.println("Authentication failed: " + e.getMessage());
    	    throw new UsernameNotFoundException("Invalid user request!", e);
    	}

    }
  
}