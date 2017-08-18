package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 攒钱计划（用户名，消费类型，消费内容，攒钱总额，建立时间，预计完成日期）
 * Created by green-cherry on 2017/8/16.
 */
public interface TaskRepository  extends JpaRepository<Task,Long> {
}
