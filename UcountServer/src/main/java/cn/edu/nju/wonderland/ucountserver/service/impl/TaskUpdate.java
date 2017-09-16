package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
import cn.edu.nju.wonderland.ucountserver.repository.TaskRepository;
import cn.edu.nju.wonderland.ucountserver.service.TaskService;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


/**
 * 更新攒钱计划的状态（定时任务）
 * Created by green-cherry on 2017/8/23.
 */

@Component
public class TaskUpdate {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskService taskService;

    //每天零点定时更新状态信息，前三位对应：秒 分 时
    @Scheduled(cron = "0 0 0 * * ? ")
    private void updateState() {
        taskRepository.updateStateToInProcess("未开始", "进行中", Date.valueOf(LocalDate.now()));
        taskRepository.updateStateToFinish("进行中", "已完成", Date.valueOf(LocalDate.now()));
        updateSavedMoney();
    }


    private void updateSavedMoney() {
        List<Task> tasks = taskRepository.findByTaskState("进行中");
        for (Task task : tasks) {
            double savedMoney = taskService.getSavedMoney(task, DateHelper.getYesterday());
            taskRepository.updateSavedMoney(task.getId(), savedMoney);
        }
    }


}
