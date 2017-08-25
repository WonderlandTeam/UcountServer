package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 *  攒钱计划（用户名，攒钱内容，攒钱总额，建立时间，预计完成日期，计划状态）
 * Created by green-cherry on 2017/8/16.
 */
public interface TaskRepository  extends JpaRepository<Task,Long> {


    /**
     * 根据用户名查找攒钱计划
     * @param username
     * @return
     */
    List<Task> findByUsername(String username);

    /**
     * 根据用户名和计划状态查找攒钱计划
     * @param username
     * @param state
     * @return
     */
    List<Task> findByUsernameAndTaskState(String username,String state);

    List<Task> findByTaskState(String state);

    /**
     * 根据用户名，计划内容，计划开始时间，结束时间 查找攒钱计划
     * @param username
     * @param taskContent
     * @param createTime
     * @param deadline
     * @return
     */
    @Query("select t from Task t where t.username=?1 and t.taskContent=?2 and t.createTime = ?3 and t.deadline = ?4")
    Task findByContentAndTime(String username, String taskContent, Date createTime, Date deadline);

    /**
     * 更新攒钱金额
     * @param id
     * @param money
     * @return
     */
    @Modifying
    @Transactional
    @Query("update Task t set t.upper = ?2 where t.id = ?1")
    int update(long id,double money);


    /**
     * 更新攒钱状态为已完成
     * @return
     */
    @Modifying
    @Transactional
    @Query("update Task t set t.taskState = ?2 where t.taskState = ?1 and t.deadline<?3")
    int updateStateToFinish(String oldState,String newState,Date today);

    /**
     * 更新攒钱状态为进行中
     * @return
     */
    @Modifying
    @Transactional
    @Query("update Task t set t.taskState = ?2 where t.taskState = ?1 and t.createTime=?3")
    int updateStateToInProcess(String oldState,String newState,Date today);

    /**
     * 更新已攒金额
     * @return
     */
    @Modifying
    @Transactional
    @Query("update Task t set t.savedMoney = ?2 where t.id=?1")
    int updateSavedMoney(Long id, Double money);
}

