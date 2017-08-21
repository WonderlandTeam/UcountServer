package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.repository.TaskRepository;
import cn.edu.nju.wonderland.ucountserver.service.TaskService;
import cn.edu.nju.wonderland.ucountserver.vo.TaskAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by green-cherry on 2017/8/21.
 */
@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    TaskRepository taskRepository;

    @Override
    public TaskInfoVO getTask(Long taskID) {
        return null;
    }

    @Override
    public List<TaskInfoVO> getTasksByUser(String username) {
        return null;
    }

    @Override
    public Long addTask(TaskAddVO taskAddVO) {
        return null;
    }

    @Override
    public void updateTask(Long taskID, TaskModifyVO taskModifyVO) {

    }

    @Override
    public void deleteTask(Long taskID) {
        taskRepository.delete(taskID);
    }
}
