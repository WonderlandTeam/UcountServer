package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;

import java.util.List;

public interface BudgetService {

    /**
     * 获取预算信息
     * @param budgetId          预算id
     * @return                  预算信息vo
     */
    BudgetInfoVO getBudget(Long budgetId);

    /**
     * 获取当月之后所有用户预算
     * @param username            用户名
     * @return                  用户预算列表
     */
    List<BudgetInfoVO> getBudgetsByUser(String username);

    /**
     * 根据月份获取用户预算
     * @param username            用户名
     * @param time                月份 格式为yyyy-mm
     * @return                  用户预算列表
     */
    List<BudgetInfoVO> getBudgetsByUserAndTime(String username,String time);


    /**
     * 添加预算
     * @param budgetAddVO       新建预算信息vo
     * @return                  预算id
     */
    Long addBudget(BudgetAddVO budgetAddVO);

    /**
     * 修改预算信息
     * @param budgetId          预算id
     * @param budgetModifyVO    预算修改信息vo
     */
    void updateBudget(Long budgetId, BudgetModifyVO budgetModifyVO);

    /**
     * 删除预算
     * @param budgetId          预算id
     */
    void deleteBudget(Long budgetId);

}
