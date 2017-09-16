package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Budget;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.BudgetRepository;
import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.service.BudgetService;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Qualifier("billServiceImpl")
    @Autowired
    private BillService billService;

    @Override
    public BudgetInfoVO getBudget(Long budgetId) {

        //获取预算并检查
        Budget budget = budgetRepository.findOne(budgetId);
        if (budget == null) {
            throw new ResourceNotFoundException("预算信息不存在");
        }

        //添加已消费金额和余额
        double consume = 0;
        consume = billService.getConsumedMoneyByTypeAndTime(budget.getUsername(), budget.getConsumeType(), DateHelper.toTimeByTimeStamp(budget.getConsumeTime()));
        return new BudgetInfoVO(budget, consume, budget.getConsumeMoney() - consume);
    }

    @Override
    public List<BudgetInfoVO> getBudgetsByUser(String username) {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth().getValue(), 1, 0, 0);

        //获取预算并检查
        List<Budget> budgets = budgetRepository.findByUsernameAndConsumeTimeGreaterThanEqual(username, Timestamp.valueOf(localDateTime));
        if ((budgets == null) || (budgets.size() == 0)) {
            throw new ResourceNotFoundException("预算信息不存在");
        }

        return getBudgetInfoVO(budgets);
    }

    /**
     * @param username 用户名
     * @param time     月份，格式为yyyy-mm
     * @return 预算列表
     */
    @Override
    public List<BudgetInfoVO> getBudgetsByUserAndTime(String username, String time) {

        Timestamp timestamp = DateHelper.toTimestampByMonth(time);

        //获取预算并检查
        List<Budget> budgets = budgetRepository.findByUsernameAndConsumeTime(username, timestamp);

        if ((budgets == null) || (budgets.size() == 0)) {
            throw new ResourceNotFoundException("预算信息不存在");
        }
        return getBudgetInfoVO(budgets);
    }

    /**
     * 将budget转化为budgetinfovo
     *
     * @param budgets 预算实体类列表
     * @return 预算vo列表
     */
    private List<BudgetInfoVO> getBudgetInfoVO(List<Budget> budgets) {
        return budgets.stream()
                .map(budget -> {
                    double consume = 0;
                    consume = billService.getConsumedMoneyByTypeAndTime(budget.getUsername(), budget.getConsumeType(), DateHelper.toTimeByTimeStamp(budget.getConsumeTime()));
                    return new BudgetInfoVO(budget, consume, budget.getConsumeMoney() - consume);
                }).collect(Collectors.toList());
    }

    @Override
    public Long addBudget(BudgetAddVO budgetAddVO) {
        String type = budgetAddVO.getConsumeType();
        String time = budgetAddVO.getConsumeTime();
        String username = budgetAddVO.getUsername();
        String regex = "[0-9]{4}-[0-9]{1,2}";

        // 参数检查
        if (type == null || time == null || username == null) {
            throw new ResourceConflictException("预算信息缺失");
        } else if (!time.matches(regex)) {
            throw new ResourceConflictException("月份格式有误，应为yyyy-MM");
        }

        long budgetID = -1;
        Budget budget = budgetRepository.findByUsernameAndConsumeTimeAndConsumeType(username, DateHelper.toTimestampByMonth(time), type);

        //判断新加的预算是否已经设置过
        //如果当月该类型预算已经有了，就只更新预算金额，否则新增预算
        if (budget != null) {
            budgetID = budget.getId();
            budgetRepository.update(budgetID, budgetAddVO.getConsumeMoney());
        } else {
            budgetID = budgetRepository.saveAndFlush(budgetAddVO.toBudgetEntity()).getId();
        }

        return budgetID;
    }

    @Override
    public void updateBudget(Long budgetId, BudgetModifyVO budgetModifyVO) {
        budgetRepository.update(budgetId, budgetModifyVO.getMoney());
    }

    @Override
    public void deleteBudget(Long budgetId) {
        budgetRepository.delete(budgetId);
    }

}
