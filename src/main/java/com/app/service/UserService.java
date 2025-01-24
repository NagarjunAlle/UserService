package com.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.entity.UserInfo;
import com.app.repository.UserInfoRepository;
import com.app.request.UserVo;

@Service
public class UserService {
	
	@Autowired
	private UserInfoRepository rep;
	
	@Autowired
	private UserVo userVo;

	public ResponseEntity<UserVo> getDetails(String uname) {
		Optional<UserInfo> optionalUserInfo = rep.findByName(uname);
	    
	    if (!optionalUserInfo.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    UserInfo userInfo = optionalUserInfo.get();
	    userVo = new UserVo(userInfo.getId(),userInfo.getEmail(),userInfo.getName(),userInfo.getPassword(),userInfo.getRoles());
	    //String responseMessage = "User details: Name: " + userInfo.getName() + ", Email: " + userInfo.getEmail();
	    
	    return new ResponseEntity<>(userVo, HttpStatus.OK);
	  }
	
	

}
