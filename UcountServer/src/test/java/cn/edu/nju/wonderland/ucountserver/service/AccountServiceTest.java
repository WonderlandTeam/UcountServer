package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    public AccountService accountService;

    @Test
    public void testGetAccountById() throws IllegalAccessException {
        AccountInfoVO vo = accountService.getAccountById(1L);
        for (Field field : vo.getClass().getFields()) {
            System.out.println(field.getName() + ":\t" + field.get(vo));
        }
    }

}
