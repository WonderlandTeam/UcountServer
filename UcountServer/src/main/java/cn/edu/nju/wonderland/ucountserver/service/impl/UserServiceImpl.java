package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.repository.UserRepository;
import cn.edu.nju.wonderland.ucountserver.service.UserService;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserModifyVO;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	
    @Override
    public Long signUp(SignUpVO signUpVO) {
    	if(userRepository.findByUsername ( signUpVO.userName ) != null){
    		return Long.valueOf(-1);
    	}
        User user = new User();
        user.setUsername(signUpVO.userName);
        user.setPassword(MD5(signUpVO.password));
        user.setTel(signUpVO.tel);
        user.setEmail(signUpVO.email);
        userRepository.save(user);
        return userRepository.findByUsername(user.getUsername()).getId();
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userRepository.findById(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.userName = user.getUsername();
        userInfoVO.email = user.getEmail();
        userInfoVO.userID = user.getId();
        userInfoVO.tel = user.getTel();
        return userInfoVO;
    }

    @Override
    public void modifyUserInfo(Long userId, UserModifyVO userModifyVO) {
        // TODO
    }

	@Override
	public UserInfoVO login(String username, String password) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username);
		if(user.getUsername() == username && user.getPassword() == MD5(password)){
	        UserInfoVO userInfoVO = new UserInfoVO();
	        userInfoVO.userName = user.getUsername();
	        userInfoVO.email = user.getEmail();
	        userInfoVO.userID = user.getId();
	        userInfoVO.tel = user.getTel();
	        return userInfoVO;
		}
		return null;
	}
	private String MD5(String str) {
        try{
        	MessageDigest md = MessageDigest.getInstance("MD5");
        	md.update(str.getBytes());
        	return new BigInteger(1, md.digest()).toString(16);
        }catch(Exception exception){
        	exception.printStackTrace();
        }
        return null;
	}
}
