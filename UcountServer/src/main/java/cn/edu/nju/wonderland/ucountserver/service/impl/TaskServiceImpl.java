package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.repository.TaskRepository;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.service.TaskService;
import cn.edu.nju.wonderland.ucountserver.vo.TaskAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by green-cherry on 2017/8/21.
 */
@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AccountService accountService;

    @Override
    public TaskInfoVO getTask(Long taskID) {

        return null;
    }

    @Override
    public List<TaskInfoVO> getTasksByState(String username, String taskState) {
        return null;
    }

    @Override
    public List<TaskInfoVO> getTasksByUser(String username) {
        return null;
    }

    @Override
    public Long addTask(TaskAddVO taskAddVO) {
        long taskID=-1;
        String username=taskAddVO.getUsername();
        String taskContent=taskAddVO.getTaskContent();
        String createTime=taskAddVO.getCreateTime();
        String deadline=taskAddVO.getDeadline();
        double upper=taskAddVO.getUpper();

        //信息检查
        if(username==null || taskContent==null || createTime==null ||deadline==null ){
            throw new ResourceConflictException("攒钱计划信息不完整");
        }

        //检查这段时间是否已有攒钱计划
        Task task=taskRepository.findByContentAndTime(username,taskContent, Timestamp.valueOf(createTime),Timestamp.valueOf(deadline));

        // 如果有并且计划正在进行中，就抛出异常
        // 如果有但不在进行中，就更新攒钱金额
        if (task!=null){
            boolean check=checkTime(task.getCreateTime().toString());
            if(check){
                throw new ResourceConflictException("此项攒钱计划正在进行中，请制定其他内容的攒钱计划。");
            }else{
                taskID=task.getId();
                taskRepository.update(taskID,taskAddVO.getUpper());
            }
        }

        // 如果没有攒钱计划，就新增攒钱计划
        else {
            taskID=taskRepository.saveAndFlush(taskAddVO.toTaskEntity()).getId();
        }

        return taskID;
    }

    @Override
    public void updateTask(Long taskID, TaskModifyVO taskModifyVO) {
        taskRepository.update(taskID,taskModifyVO.getMoney());
    }

    @Override
    public void deleteTask(Long taskID) {
        taskRepository.delete(taskID);
    }

    /**
     * 判断攒钱计划状态是否应该变为正在进行中
     * @param time
     * @return
     */
    private boolean checkTime(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date bt= null;
        Date today=null;
        boolean check=true;
        try {
            bt = sdf.parse(time);
            today=sdf.parse(new Date().toString());
            if (bt.before(today)){
                check = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return check;
    }

    /**
     * 获得剩余的天数
     * @param time
     * @return
     */
    private int getRemainDays(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date dtime= null;
        Date today=null;
        int days=0;
        try {
            dtime = sdf.parse(time);
            today=sdf.parse(new Date().toString());
            days= (int)(today.getTime()-dtime.getTime())/24*60*60*1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 计算每日应攒金额
     * 每日应攒金额（(目标金额-已攒)/剩下时间）
     *
     * @param upper
     * @param savedMoney
     * @param remainDays
     * @return
     */
    private double getHaveToSave(double upper,double savedMoney,int remainDays){
        double haveToSave=0;
        if(remainDays!=0){
            haveToSave = (upper-savedMoney)/remainDays;
        }
        return haveToSave;
    }

    /**
     * 计算已攒金额
     * 已攒金额（用每日理论平均消费（日平均=账户余额/剩余天数）-当日实际消费）
     *
     * @param username
     * @param remainDays
     * @return
     */
    private double getSavedMoney(String username, int remainDays,double upper){
        double savedMoney = 0;
        double remainedMoney=accountService.getBalanceByUser(username);
        double consumedMoney=accountService.getConsumedMoneyByDateAndUser(username,getToday());

        if(remainDays!=0){
            savedMoney=remainedMoney/remainDays-consumedMoney;
        }else{
            savedMoney=upper;
        }
        return savedMoney;
    }

    /**
     * 获得日期格式yyyy-MM-dd的日期
     * @return
     */
    private String getToday(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date today=null;
        try {
            today=sdf.parse(new Date().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today.toString();
    }
}
