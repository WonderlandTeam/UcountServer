package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
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
     * 根据状态获取所有攒钱计划预算
     * @param username          用户名
     * @param taskState         计划状态
     * @return                  用户计划列表
     */
    List<TaskInfoVO> getTasksByState(String username,String taskState);


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

    /**
     * 计算已攒金额
     * 已攒金额（用每日理论平均消费（日平均=账户余额/剩余天数）-当日实际消费）
     *
     * @param task
     * @param date
     * @return
     */
    double getSavedMoney(Task task, String date);
}
