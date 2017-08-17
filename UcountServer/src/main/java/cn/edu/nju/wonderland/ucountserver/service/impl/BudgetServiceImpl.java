package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.service.BudgetService;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Override
    public BudgetInfoVO getBudget(Long budgetId) {
        return null;
    }

    @Override
    public List<BudgetInfoVO> getBudgetsByUser(Long userId) {
        return null;
    }

    @Override
    public Long addBudget(BudgetAddVO budgetAddVO) {
        return null;
    }

    @Override
    public void updateBudget(Long budgetId, BudgetModifyVO budgetModifyVO) {

    }

    @Override
    public void deleteBudget(Long budgetId) {

    }
}
