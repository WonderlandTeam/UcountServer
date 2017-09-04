package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.ManualBilling;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 手动记账（用户名，时间，收/支，消费项目，消费类型，备注，卡类型，账号）
 * Created by green-cherry on 2017/8/16.
 */
public interface ManualBillingRepository extends JpaRepository<ManualBilling, Long> {
    ManualBilling findByIdAndCardId(Long id, String cardId);
}
