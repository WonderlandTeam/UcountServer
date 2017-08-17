package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    /**
     * 获取预算信息
     */
    @GetMapping("/{budget_id}")
    public Map<String, Object> getBudget(@PathVariable("budget_id") Long budgetId) {
        return null;
    }

    /**
     * 获取用户预算
     */
    @GetMapping
    public Map<String, Object> getBudgetsByUser(@RequestParam Long userId) {
        return null;
    }

    /**
     * 添加预算
     */
    @PostMapping
    public Map<String, Object> addBudget(@RequestBody BudgetAddVO budgetAddVO) {
        return null;
    }

    /**
     * 修改预算
     */
    @PutMapping("/{budget_id}")
    public Map<String, Object> updateBudget(@PathVariable("budget_id") Long budgetId,
                                            @RequestBody BudgetModifyVO budgetModifyVO) {
        return null;
    }

    /**
     * 删除预算
     */
    @DeleteMapping("/{budget_id}")
    public Map<String, Object> deleteBudget(@PathVariable("budget_id") Long budgetId) {
        return null;
    }

}
