package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 预算（用户名，消费类型，消费金额，预算时间，建立时间）
 * Created by green-cherry on 2017/8/16.
 */
public interface BudgetRepository extends JpaRepository<Budget,Long> {
}
