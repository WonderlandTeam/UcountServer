package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatementServiceTest {

    @Autowired
    public StatementService statementService;

    @Test
    public void testGetIncomeStatement() throws IllegalAccessException {
        IncomeStatementVO vo = statementService.getIncomeStatement("sigma", "2017-06-01", "2017-07-01");
        for (Field field : vo.getClass().getFields()) {
            System.out.println(field.getName() + ":\t" + field.get(vo));
        }
    }

}
