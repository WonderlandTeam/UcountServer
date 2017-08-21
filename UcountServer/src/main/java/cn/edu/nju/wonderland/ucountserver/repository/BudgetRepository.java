package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 预算（用户名，消费类型，消费金额，预算时间，建立时间）
 * Created by green-cherry on 2017/8/16.
 */
public interface BudgetRepository extends JpaRepository<Budget,Long> {


    /**
     * 获取当月之后的所有预算信息
     * @param username
     * @param time
     * @return
     */
    List<Budget> findByUsernameAndConsumeTimeGreaterThanEqual(String username,Timestamp time);

    /**
     * 按月份获取预算信息
     * @param username
     * @param time
     * @return
     */
    List<Budget> findByUsernameAndConsumeTime(String username, Timestamp time);

    /**
     * 根据用户名，预算时间，预算类型获取预算信息
     * @param username
     * @param time
     * @param type
     * @return
     */
    Budget findByUsernameAndConsumeTimeAndConsumeType(String username,Timestamp time,String type);

    /**
     * 更新当月预算
     * @param id
     * @param money
     * @return
     */
    @Modifying
    @Query("update Budget b set b.consumeMoney = ?2 where b.id = ?1")
    int update(long id,double money);


}
