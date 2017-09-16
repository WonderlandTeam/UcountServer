package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.TaskRepository;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.service.TaskService;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import cn.edu.nju.wonderland.ucountserver.vo.TaskAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskModifyVO;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by green-cherry on 2017/8/21.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    private AccountService accountService;

    public TaskServiceImpl(TaskRepository taskRepository, AccountService accountService) {
        this.taskRepository = taskRepository;
        this.accountService = accountService;
    }

    @Override
    public TaskInfoVO getTask(Long taskID) {
        Task task = taskRepository.findOne(taskID);
        if (task == null) {
            throw new ResourceNotFoundException("攒钱计划不存在");
        }
        return getTaskInfo(task);
    }

    @Override
    public List<TaskInfoVO> getTasksByState(String username, String taskState) {
        List<Task> tasks = taskRepository.findByUsernameAndTaskState(username, taskState);
        if (tasks == null || tasks.size() == 0) {
            throw new ResourceNotFoundException("攒钱计划不存在");
        }
        return tasks.stream().map(this::getTaskInfo).collect(Collectors.toList());
    }

    @Override
    public List<TaskInfoVO> getTasksByUser(String username) {
        List<Task> tasks = taskRepository.findByUsername(username);
        if (tasks == null || tasks.size() == 0) {
            throw new ResourceNotFoundException("攒钱计划不存在");
        }
        return tasks.stream().map(this::getTaskInfo).collect(Collectors.toList());
    }

    @Override
    public Long addTask(TaskAddVO taskAddVO) {
        long taskID = -1;
        String username = taskAddVO.getUsername();
        String taskContent = taskAddVO.getTaskContent();
        String createTime = taskAddVO.getCreateTime();
        String deadline = taskAddVO.getDeadline();

        String regex = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";

        //信息检查
        if (username == null || taskContent == null || createTime == null || deadline == null) {
            throw new ResourceConflictException("攒钱计划信息不完整");
        } else if ((!createTime.matches(regex)) || (!deadline.matches(regex))) {
            throw new ResourceConflictException("日期格式有误，应为yyyy-MM-dd");
        } else if (Date.valueOf(createTime).before(Date.valueOf(DateHelper.getTodayDate()))) {
            throw new ResourceConflictException("攒钱计划开始时间不能设定在今天之前");
        }

        //检查这段时间是否已有攒钱计划
        Task task = taskRepository.findByContentAndTime(username, taskContent, Date.valueOf(createTime), Date.valueOf(deadline));

        // 如果有并且计划正在进行中，就抛出异常
        // 如果有但不在进行中，就更新攒钱金额
        if (task != null) {
            boolean check = checkTime(task.getCreateTime());
            if (check) {
                throw new ResourceConflictException("此项攒钱计划正在进行中，请制定其他内容的攒钱计划。");
            } else {
                taskID = task.getId();
                taskRepository.update(taskID, taskAddVO.getUpper());
            }
        }

        // 如果没有攒钱计划，就新增攒钱计划
        else {
            taskID = taskRepository.saveAndFlush(taskAddVO.toTaskEntity()).getId();
        }

        return taskID;
    }

    @Override
    public void updateTask(Long taskID, TaskModifyVO taskModifyVO) {
        taskRepository.update(taskID, taskModifyVO.getMoney());
    }

    @Override
    public void deleteTask(Long taskID) {
        taskRepository.delete(taskID);
    }

    /**
     * 获得taskinfovo
     *
     * @param task
     * @return
     */
    private TaskInfoVO getTaskInfo(Task task) {
        double savedMoney = task.getSavedMoney();
        double haveToSave = 0;
        double upper = task.getUpper();
        String state = task.getTaskState();

        if (state.equals("进行中")) {
            int remainDays = getRemainDays(task.getDeadline());
            savedMoney = getSavedMoney(task, DateHelper.getToday());
            haveToSave = getHaveToSave(upper, savedMoney, remainDays);
        }
        return new TaskInfoVO(task, savedMoney, haveToSave);
    }

    /**
     * 判断攒钱计划状态是否应该变为正在进行中
     * 如果应该变，即传入时间到了今天，就返回true，否则返回false
     *
     * @param date  日期
     * @return      true/false
     */
    private boolean checkTime(Date date) {
        Date today = Date.valueOf(LocalDate.now());
        return date.equals(today);
    }

    /**
     * 获得剩余的天数
     *
     * @param date  日期
     * @return      剩余天数
     */
    private int getRemainDays(Date date) {
        Date today = Date.valueOf(DateHelper.getTodayDate());
        long days = (date.getTime() - today.getTime()) / (24 * 60 * 60 * 1000) + 1;
        return (int) days;
    }

    /**
     * 计算每日应攒金额
     * 每日应攒金额（(目标金额-已攒)/剩下时间）
     */
    private double getHaveToSave(double upper, double savedMoney, int remainDays) {
        double haveToSave = 0;
        if (remainDays != 0) {
            haveToSave = (upper - savedMoney) / remainDays;
        }
        return haveToSave;
    }

    /**
     * 计算已攒金额
     * 已攒金额（用每日理论平均消费（日平均=账户余额/剩余天数）-当日实际消费）
     *
     * @param task  任务实体
     * @return      日期
     */
    public double getSavedMoney(Task task, String date) {

        String username = task.getUsername();
        int remainDays = getRemainDays(task.getDeadline());
        double savedMoney = task.getSavedMoney();

        double remainedMoney = accountService.getBalanceByUser(username);
        double consumedMoney = accountService.getConsumedMoneyByDateAndUser(username, date);

        if (remainDays != 0) {
            savedMoney += remainedMoney / remainDays - consumedMoney;
        }

        return savedMoney;
    }


}

