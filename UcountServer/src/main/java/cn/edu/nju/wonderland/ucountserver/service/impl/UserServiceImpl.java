package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.repository.UserRepository;
import cn.edu.nju.wonderland.ucountserver.service.UserService;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserModifyVO;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	
    @Override
    public String signUp(SignUpVO signUpVO) {
    	if(userRepository.findByUsername ( signUpVO.userName ) != null){
    		return null;
    	}
        User user = new User();
        user.setUsername(signUpVO.userName);
        user.setPassword(MD5(signUpVO.password));
        user.setTel(signUpVO.tel);
        user.setEmail(signUpVO.email);
        userRepository.save(user);
        return user.getUsername();
    }

    @Override
    public UserInfoVO getUserInfo(String username) {
        User user = userRepository.findByUsername(username);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.userName = user.getUsername();
        userInfoVO.email = user.getEmail();
        userInfoVO.tel = user.getTel();
        return userInfoVO;
    }

    @Override
    public void modifyUserInfo(String username, UserModifyVO userModifyVO) {
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
