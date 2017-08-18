package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Remind;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 提醒（用户名，提醒类型，提醒时间）
 * Created by green-cherry on 2017/8/17.
 */
public interface RemindRepository  extends JpaRepository<Remind,Long> {
}
