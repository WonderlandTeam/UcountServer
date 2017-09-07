package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 支付宝（用户名，账号，交易号 ，商户订单号，交易创建时间，付款时间，最近修改时间，交易来源地，交易类型，交易对方，商品名称 ，金额，收/支，交易状态，服务费，成功退款，备注，资金状态，消费类型）
 * Created by green-cherry on 2017/8/16.
 */
public interface AlipayRepository extends JpaRepository<Alipay, Long> {
    Alipay findByIdAndCardId(Long id, String cardId);

    Page<Alipay> findByCardId(String cardId, Pageable pageable);

    List<Alipay> findByUsername(String username, Pageable pageable);

    void deleteByCardId(String cardId);

    @Query("SELECT p FROM Alipay p WHERE p.payTime = (SELECT max(p2.payTime) FROM Alipay p2 WHERE p2.cardId = ?1 and p2.payTime < ?2) and p.cardId = ?1")
    Alipay getBalance(String account,Timestamp time);

    List<Alipay> findByUsernameAndPayTimeBetween(String username, Timestamp start, Timestamp end);
}
