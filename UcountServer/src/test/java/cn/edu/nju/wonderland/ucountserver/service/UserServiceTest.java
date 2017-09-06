package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.service.impl.UserServiceImpl;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void testSignUp() {
        SignUpVO signUpVO = new SignUpVO();
        signUpVO.userName = "456";
        signUpVO.email = "123@qq.com";
        signUpVO.password = MD5("123");
        signUpVO.tel = "123";
        String username = userService.signUp(signUpVO);
        assertEquals(username, "456");
    }

    @Test
    public void testLogin() {
        UserInfoVO userInfoVO = userService.login("456", MD5("123"));
        assertEquals(userInfoVO.userName, "456");
    }

    @Test
    public void testGetUserInfo() {
        UserInfoVO userInfoVO = userService.getUserInfo("456");
        assertEquals(userInfoVO.userName, "456");
    }

    @Test
    public void testModefyUserInfo() {
        UserInfoVO user = new UserInfoVO();
        user.tel = "789";
        user.userName = "456";
        user.password = MD5("123");
        user.email = "456@qq.com";
        userService.modifyUserInfo(user.userName, user);
        user = userService.getUserInfo("456");
        assertEquals(user.email, "456@qq.com");
        assertEquals(user.tel, "789");
    }

    private String MD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
