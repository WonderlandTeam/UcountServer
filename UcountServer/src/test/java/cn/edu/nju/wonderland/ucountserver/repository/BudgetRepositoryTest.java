package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Budget;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by green-cherry on 2017/8/23.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class BudgetRepositoryTest {
    @Autowired
    BudgetRepository budgetRepository;

    /**
     * 测试保存预算
     */
    @Test
    public void testSaveAndFlush() {
//        String username="sense";
//        String consumeType="交通";
//        double consumeMoney=100;
//        String consumeTime="2017-7";
//        Budget budget=new Budget(username,consumeType,consumeMoney, DateHelper.toTimestampByMonth(consumeTime), Timestamp.valueOf(LocalDateTime.now()));
//        Budget newBudget=budgetRepository.saveAndFlush(budget);
//        System.out.println(newBudget.getId());

    }

    /**
     * 测试根据ID获取预算
     */
    @Test
    public void testFindOne() {
        Budget budget = budgetRepository.findOne(1L);
        System.out.println(budget.getUsername());
        System.out.println(budget.getConsumeType());
        System.out.println(budget.getConsumeMoney());
        System.out.println(budget.getConsumeTime());
    }

    /**
     * 测试获取当月之后的所有预算信息
     */
    @Test
    public void testFindByUsernameAndConsumeTimeGreaterThanEqual() {
        String username = "sense";
        Timestamp timestamp = DateHelper.toTimestampByMonth("2017-7");
        List<Budget> budgets = budgetRepository.findByUsernameAndConsumeTimeGreaterThanEqual(username, timestamp);
        for (Budget budget : budgets) {
            System.out.println(budget.getId());
            System.out.println(budget.getUsername());
            System.out.println(budget.getConsumeType());
            System.out.println(budget.getConsumeMoney());
            System.out.println(budget.getConsumeTime());
        }
    }

    /**
     * 测试按月份获取预算信息
     */
    @Test
    public void testFindByUsernameAndConsumeTime() {
        String username = "sense";
        Timestamp timestamp = DateHelper.toTimestampByMonth("2017-9");
        List<Budget> budgets = budgetRepository.findByUsernameAndConsumeTime(username, timestamp);
        for (Budget budget : budgets) {
            System.out.println(budget);
        }
    }

    /**
     * 测试根据用户名，预算时间，预算类型获取预算信息
     */
    @Test
    public void testFindByUsernameAndConsumeTimeAndConsumeType() {
        String username = "sense";
        Timestamp timestamp = DateHelper.toTimestampByMonth("2017-8");
        String consumeType = "交通";
        Budget budget = budgetRepository.findByUsernameAndConsumeTimeAndConsumeType(username, timestamp, consumeType);
        System.out.println(budget);
    }

    /**
     * 测试更新预算
     */
    @Test
    public void testUpdate() {
        int i = budgetRepository.update(1L, 150);
        System.out.println(i);
    }

    /**
     * 测试删除预算
     */
    @Test
    public void testDelete() {
        budgetRepository.delete(4L);
    }
}
