package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

/**
 *  攒钱计划（用户名，攒钱内容，攒钱总额，建立时间，预计完成日期，计划状态）
 * Created by green-cherry on 2017/8/16.
 */
public interface TaskRepository  extends JpaRepository<Task,Long> {



    List<Task> findByUsername(String username);

    List<Task> findByUsernameAndTaskState(String username,String state);

    @Query("select t from Task t where t.username=?1 and t.taskContent=?2 and t.createTime = ?3 and t.deadline = ?4")
    Task findByContentAndTime(String username, String taskContent, Date createTime, Date deadline);

    /**
     * 更新攒钱金额
     * @param id
     * @param money
     * @return
     */
    @Modifying
    @Query("update Task t set t.upper = ?2 where t.id = ?1")
    int update(long id,double money);


    /**
     * 更新攒钱状态为已完成
     * @return
     */
    @Modifying
    @Query("update Task t set t.taskState = ?2 where t.taskState = ?1 and t.deadline=today")
    int updateStateToFinish(String oldState,String newState,Date today);

    /**
     * 更新攒钱状态为进行中
     * @return
     */
    @Modifying
    @Query("update Task t set t.taskState = ?2 where t.taskState = ?1 and t.createTime=today")
    int updateStateToInProcess(String oldState,String newState,Date today);
}

