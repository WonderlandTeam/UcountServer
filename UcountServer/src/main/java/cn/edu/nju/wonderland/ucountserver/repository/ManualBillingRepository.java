package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.ManualBilling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 手动记账（用户名，时间，收/支，消费项目，消费类型，备注，卡类型，账号）
 * Created by green-cherry on 2017/8/16.
 */
public interface ManualBillingRepository extends JpaRepository<ManualBilling, Long> {

    /*
     * 注：手动记账账户须通过用户名username、账户类型cardType、账户名称cardId唯一确定
     */

    ManualBilling findByUsernameAndCardTypeAndCardIdAndId(String username, String cardType, String cardId, Long id);

    List<ManualBilling> findByUsernameAndCardTypeAndCardId(String username, String cardType, String cardId);

    void deleteByUsernameAndCardTypeAndCardId(String username, String cardType, String cardId);

    List<ManualBilling> findByUsernameAndTimeBetween(String username, Timestamp start, Timestamp end);

    @Query( "SELECT s " +
            "FROM ManualBilling s " +
            "WHERE s.username = ?1 and s.cardType = ?2 and s.cardId = ?3 and" +
            "       s.time = (  SELECT max(p2.time) " +
            "                   FROM ManualBilling p2 " +
            "                   WHERE p2.username = ?1 and p2.cardType = ?2 and p2.cardId = ?3 and p2.time <= ?4)")
    List<ManualBilling> getBalance(String username, String cardType, String cardId, Timestamp timestamp);
}
