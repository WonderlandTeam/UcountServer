package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;


/**
 * 更新攒钱计划的状态
 * Created by green-cherry on 2017/8/23.
 */

@Component
public class TaskUpdate {

    @Autowired
    TaskRepository taskRepository;

    @Scheduled(cron = "0 0 0 * * ? ")//每天零点更新状态信息
    private void updateState(){
        taskRepository.updateStateToInProcess("未开始","进行中", Date.valueOf(LocalDate.now()));
        taskRepository.updateStateToFinish("进行中","已完成", Date.valueOf(LocalDate.now()));
    }
}
