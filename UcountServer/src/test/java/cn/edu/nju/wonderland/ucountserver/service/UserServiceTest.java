package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserServiceTest {
    UserService userService;
    @Test
    public void testSignUp(){
        SignUpVO signUpVO = new SignUpVO();
        signUpVO.userName = "123";
        signUpVO.email = "123@qq.com";
        signUpVO.password = "123";
        signUpVO.tel = "123";
        String username = userService.signUp(signUpVO);
        assertEquals(username,"123");
    }
    public  void testLogin(){
        UserInfoVO userInfoVO = userService.login("123","123");
        assertEquals(userInfoVO.userName,"123");
    }
    public void testGetUserInfo(){
        UserInfoVO  userInfoVO = userService.getUserInfo("123");
        assertEquals(userInfoVO.userName,"123");
    }
}
