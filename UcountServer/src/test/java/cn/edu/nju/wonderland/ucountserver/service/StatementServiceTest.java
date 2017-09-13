package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.CashFlowItemVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatementServiceTest {

    @Autowired
    public StatementService statementService;

    @Test
    public void testGetIncomeStatement() throws IllegalAccessException {
        IncomeStatementVO vo = statementService.getIncomeStatement("sigma", "2017-06-01", "2017-07-01");
        for (Field field : vo.getClass().getFields()) {
            System.out.println(field.getName() + ": " + field.get(vo));
        }
    }

    @Test
    public void testGetBalanceSheet() throws IllegalAccessException {
        BalanceSheetVO vo = statementService.getBalanceSheet("sigma", "2017-08-01");
        for (Field field : vo.getClass().getFields()) {
            System.out.println(field.getName() + ": " + field.get(vo));
        }
    }

    @Test
    public void testGetCashFlows() {
        List<CashFlowItemVO> cashFlows = statementService.getCashFlows("sigma", "2017-07-20", "2017-08-01");
        cashFlows.forEach(v -> System.out.println(v.time + " " + v.money + "\t" + v.billType + "\t" + v.cardId));
    }

}
