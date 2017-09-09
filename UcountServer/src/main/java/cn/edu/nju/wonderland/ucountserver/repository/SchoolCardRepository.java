package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 校园卡（用户名username，账号card_id，消费方式，地点location，消费顺序，时间，收/支income_expenditure，余额，消费类型consume_type）
 * Created by green-cherry on 2017/8/16.
 */
public interface SchoolCardRepository extends JpaRepository<SchoolCard, Long> {
    SchoolCard findByIdAndCardId(Long id, String cardId);

    Page<SchoolCard> findByCardId(String cardId, Pageable pageable);

    List<SchoolCard> findByUsername(String username, Pageable pageable);

    void deleteByCardId(String cardId);

    @Query("SELECT s FROM SchoolCard s WHERE s.cardId = ?1 and s.time = (SELECT max(p2.time) FROM SchoolCard p2 WHERE p2.cardId = ?1 and p2.time <= ?2)")
    List<SchoolCard> getBalance(String cardId, Timestamp time);

    List<SchoolCard> findByUsernameAndTimeBetween(String username, Timestamp start, Timestamp end);
}
