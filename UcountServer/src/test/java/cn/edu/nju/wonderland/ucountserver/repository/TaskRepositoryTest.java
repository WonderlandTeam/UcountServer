package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;

/**
 * Created by green-cherry on 2017/8/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskRepositoryTest {
    @Autowired
    TaskRepository taskRepository;

    /**
     * 测试保存计划
     */
    @Test
    public void testSaveAndFlush() {
//        String username="sense";
//        String taskContent="学车";
//        String createTime="2017-9-12";
//        String deadline= "2017-11-1";
//        Double upper=2000.0;
//        Task task=new Task(username, Date.valueOf(createTime), taskContent, Date.valueOf(deadline), upper, "未开始");
//        Task task1=taskRepository.saveAndFlush(task);
    }

    /**
     * 测试根据id查找计划
     */
    @Test
    public void testFindOne() {
        Task task=taskRepository.findOne(1L);
        System.out.println(task);
    }

    /**
     * 测试根据用户名查找攒钱计划
     */
    @Test
    public void testFindByUsername() {
        String username="sense";
        List<Task> tasks=taskRepository.findByUsername(username);
        for (Task task:tasks){
            System.out.println(task);
        }
    }

    /**
     * 测试根据用户名和计划状态查找攒钱计划
     */
    @Test
    public void testFindByUsernameAndTaskState() {
        String username="sense";
        String taskState="未开始";
        List<Task> tasks=taskRepository.findByUsernameAndTaskState(username,taskState);
        for (Task task:tasks){
            System.out.println(task);
        }
    }

    /**
     * 测试根据用户名，计划内容，计划开始时间，结束时间 查找攒钱计划
     */
    @Test
    public void testFindByContentAndTime() {
//        String username="sense";
//        String taskContent="学车";
//        String createTime="2017-9-1";
//        String deadline= "2017-9-1";
//        Task task1=taskRepository.findByContentAndTime(username,taskContent,Date.valueOf(createTime),Date.valueOf(deadline));
//        System.out.println(task1);
    }

    /**
     * 测试更新攒钱金额
     */
    @Test
    public void testUpdate() {
//        taskRepository.update(1l,3000);
    }

    /**
     * 测试更新攒钱状态为已完成
     */
    @Test
    public void testUpdateStateToFinish() {
        String oldState="进行中";
        String newState="已完成";
        Date date=Date.valueOf( DateHelper.getTodayDate());
        taskRepository.updateStateToFinish(oldState,newState,date);
    }

    /**
     * 测试更新攒钱状态为进行中
     */
    @Test
    public void testUpdateStateToInProcess() {
        String oldState="未开始";
        String newState="进行中";
        Date date=Date.valueOf( DateHelper.getTodayDate());
        taskRepository.updateStateToInProcess(oldState,newState,date);
    }
}
