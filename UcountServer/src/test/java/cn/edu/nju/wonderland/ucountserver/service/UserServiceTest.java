package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.security.MessageDigest;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    public void testSignUp() {
        SignUpVO signUpVO = new SignUpVO();
        signUpVO.userName = "456";
        signUpVO.email = "123@qq.com";
        signUpVO.password = "123";
        signUpVO.tel = "123";
        String username = userService.signUp(signUpVO, null);
        assertEquals(username, "456");
    }

    @Test
    public void testLogin() {
        UserInfoVO userInfoVO = userService.login("123", "123", null);
        assertEquals(userInfoVO.userName, "123");
    }

    @Test
    public void testGetUserInfo() {
        UserInfoVO userInfoVO = userService.getUserInfo("456");
        assertEquals(userInfoVO.userName, "456");
    }

    @Test
    public void testModefyUserInfo() {
        UserInfoVO user = new UserInfoVO();
        user.tel = "15050582710";
        user.userName = "456";
        user.password = "123";
        user.email = "151250089@smail.nju.edu.cn";
        userService.modifyUserInfo(user.userName, user);
        user = userService.getUserInfo("456");
        assertEquals(user.email, "151250089@smail.nju.edu.cn");
        assertEquals(user.tel, "15050582710");
    }
    @Test
    public void testfindPasswordByMail(){
        String result = userService.findPasswordByMail("456");
        System.out.println(result);
    }
    @Test
    public void testFindPasswordByTel() {
        String result = userService.findPasswordByTel("456");
        System.out.println(result);
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
