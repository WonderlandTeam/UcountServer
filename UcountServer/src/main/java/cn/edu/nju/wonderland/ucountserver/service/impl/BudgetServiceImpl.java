package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Budget;
import cn.edu.nju.wonderland.ucountserver.repository.BudgetRepository;
import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.service.BudgetService;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private BillService billService;

    @Override
    public BudgetInfoVO getBudget(Long budgetId) {
        Budget budget = budgetRepository.findOne(budgetId);
        double consume = 0;
        consume=billService.getConsumedMoneyByTypeAndTime(budget.getUsername(),budget.getConsumeType(),budget.getConsumeTime().toString());
        BudgetInfoVO budgetInfoVO = new BudgetInfoVO(budget, consume, budget.getConsumeMoney() - consume);
        return budgetInfoVO;
    }

    @Override
    public List<BudgetInfoVO> getBudgetsByUser(String username) {
        LocalDateTime localDateTime=LocalDateTime.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonth().getValue(),1,0,0);
        List<Budget> budgets = budgetRepository.findByUsernameAndConsumeTimeGreaterThanEqual(username, Timestamp.valueOf(localDateTime));
        return getBudgetInfoVO(budgets);
    }

    @Override
    public List<BudgetInfoVO> getBudgetsByUserAndTime(String username, String time) {
        List<Budget> budgets = budgetRepository.findByUsernameAndConsumeTime(username, Timestamp.valueOf(time));
        return getBudgetInfoVO(budgets);
    }

    /**
     * 将budget转化为budgetinfovo
     * @param budgets
     * @return
     */
    private List<BudgetInfoVO> getBudgetInfoVO(List<Budget> budgets){
        return budgets.stream()
                .map(budget -> {
                    double consume = 0;
                    consume=billService.getConsumedMoneyByTypeAndTime(budget.getUsername(),budget.getConsumeType(),budget.getConsumeTime().toString());
                    return new BudgetInfoVO(budget, consume, budget.getConsumeMoney() - consume);
                }).collect(Collectors.toList());
    }

    @Override
    public Long addBudget(BudgetAddVO budgetAddVO) {

        //判断新加的预算是否已经设置过
        String type = budgetAddVO.getConsumeType();
        String time = budgetAddVO.getConsumeTime();
        String username = budgetAddVO.getUsername();
        long budgetID = -1;
        Budget budget = budgetRepository.findByUsernameAndConsumeTimeAndConsumeType(username, Timestamp.valueOf(time), type);

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
