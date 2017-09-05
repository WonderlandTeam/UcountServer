package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.service.impl.UserServiceImpl;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
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
    @Test
    public  void testLogin(){
        UserInfoVO userInfoVO = userService.login("123","123");
        assertEquals(userInfoVO.userName,"123");
    }
    @Test
    public void testGetUserInfo(){
        UserInfoVO  userInfoVO = userService.getUserInfo("123");
        assertEquals(userInfoVO.userName,"123");
    }
}
