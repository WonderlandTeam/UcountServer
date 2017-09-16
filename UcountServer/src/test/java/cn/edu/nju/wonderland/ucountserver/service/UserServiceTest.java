package cn.edu.nju.wonderland.ucountserver.service;

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

    public void testSignUp() {
//        SignUpVO signUpVO = new SignUpVO();
//        signUpVO.userName = "456";
//        signUpVO.email = "123@qq.com";
//        signUpVO.password = "123";
//        signUpVO.tel = "123";
//        userService.signUp(signUpVO);
    }

    @Test
    public void testLogin() {
        UserInfoVO userInfoVO = userService.login("sigma", "sigma", null);
        assertEquals(userInfoVO.userName, "sigma");
    }

    @Test
    public void testGetUserInfo() {
        UserInfoVO userInfoVO = userService.getUserInfo("sigma");
        assertEquals(userInfoVO.userName, "sigma");
    }

    @Test
    public void testModefyUserInfo() {
//        UserInfoVO user = new UserInfoVO();
//        user.tel = "15050582710";
//        user.userName = "456";
//        user.password = "123";
//        user.email = "151250089@smail.nju.edu.cn";
//        userService.modifyUserInfo(user.userName, user);
//        user = userService.getUserInfo("456");
//        assertEquals(user.email, "151250089@smail.nju.edu.cn");
//        assertEquals(user.tel, "15050582710");
    }

    @Test
    public void testfindPasswordByMail() {
        String result = userService.findPasswordByMail("456");
        System.out.println(result);
    }

    @Test
    public void testFindPasswordByTel() {
        String result = userService.findPasswordByTel("456");
        System.out.println(result);
    }

}
