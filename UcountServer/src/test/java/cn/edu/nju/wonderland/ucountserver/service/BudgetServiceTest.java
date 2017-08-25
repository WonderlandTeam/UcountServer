package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by green-cherry on 2017/8/23.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class BudgetServiceTest {
    @Autowired
    BudgetService budgetService;

    @Test
    public void testGetBudget(){
        BudgetInfoVO budgetInfoVO=budgetService.getBudget(1l);
        System.out.println(budgetInfoVO);

    }

    @Test
    public void testGetBudgetsByUser(){
        List<BudgetInfoVO> budgetInfoVOS=budgetService.getBudgetsByUser("sense");
        for(BudgetInfoVO budgetInfoVO:budgetInfoVOS){
            System.out.println(budgetInfoVO);
        }
    }

    @Test
    public void testGetBudgetsByUserAndTime(){
        String username="sense";
        String time="2017-7";
        List<BudgetInfoVO> budgetInfoVOS=budgetService.getBudgetsByUserAndTime(username,time);
        for(BudgetInfoVO budgetInfoVO:budgetInfoVOS){
            System.out.println(budgetInfoVO);
        }
    }

    @Test
    public void testAddBudget(){
        String username="sense";
        String consumeType="饮食";
        double consumeMoney=1;
        String consumeTime="2017-8";
        BudgetAddVO budgetAddVO=new BudgetAddVO(username,consumeType,consumeMoney,consumeTime);
        budgetService.addBudget(budgetAddVO);
    }

    @Test
    public void testUpdateBudget(){
        Long id=1l;
        BudgetModifyVO budgetModifyVO=new BudgetModifyVO(1000);
        budgetService.updateBudget(id,budgetModifyVO);
    }

    @Test
    public void testDeleteBudget(){
        budgetService.deleteBudget(4L);
    }
}
