package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.TaskAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskModifyVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by green-cherry on 2017/8/23.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {
    @Autowired
    TaskService taskService;

    @Test
    public void testGetTask(){
        TaskInfoVO taskInfoVO=taskService.getTask(1l);
        System.out.println(taskInfoVO);
    }

    @Test
    public void testGetTasksByState(){
        List<TaskInfoVO> taskInfoVOS=taskService.getTasksByState("sense","已完成");
        for(TaskInfoVO taskInfoVO:taskInfoVOS){
            System.out.println(taskInfoVO);
        }
    }

    @Test
    public void testGetTasksByUser(){
        List<TaskInfoVO> taskInfoVOS=taskService.getTasksByUser("sense");
        for(TaskInfoVO taskInfoVO:taskInfoVOS){
            System.out.println(taskInfoVO);
        }
    }

    @Test
    public void testAddTask(){
        String username="sense";
        String taskContent="买房";
        String createTime="2017-8-25";
        String deadline="2019-7-1";
        Double upper=2000000.0;
        TaskAddVO taskAddVO=new TaskAddVO(username,taskContent,createTime,deadline,upper);
        long i =taskService.addTask(taskAddVO);
        System.out.println(i);
    }

    @Test
    public void testUpdateTask(){
        Long id=1l;
        TaskModifyVO taskModifyVO=new TaskModifyVO(1000);
        taskService.updateTask(id,taskModifyVO);
    }

    @Test
    public void testDeleteTask(){
        taskService.deleteTask(6L);
    }
}
