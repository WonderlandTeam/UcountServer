package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.TaskAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskModifyVO;

import java.util.List;

public interface TaskService {

    /**
     * 获取预算信息
     * @param taskID          计划id
     * @return                计划信息vo
     */
    TaskInfoVO getTask(Long taskID);

    /**
     * 获取所有攒钱计划预算
     * @param username          用户名
     * @return                  用户计划列表
     */
    List<TaskInfoVO> getTasksByUser(String username);


    /**
     * 添加攒钱计划
     * @param taskAddVO         新建计划信息vo
     * @return                  计划id
     */
    Long addTask(TaskAddVO taskAddVO);

    /**
     * 修改攒钱计划信息
     * @param taskID            计划id
     * @param taskModifyVO      攒钱计划vo
     */
    void updateTask(Long taskID, TaskModifyVO taskModifyVO);

    /**
     * 删除攒钱计划
     * @param taskID            计划id
     */
    void deleteTask(Long taskID);
}
