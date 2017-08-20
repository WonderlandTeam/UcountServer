package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 工行卡（用户名，账号，交易日期，摘要，交易场所，交易国家或地区简称，钞/汇，交易金额(收入)，交易金额(支出)，交易币种，记账金额(收入)，记账金额(支出)，记账币种，余额，对方户名，消费类型）
 * Created by green-cherry on 2017/8/16.
 */
public interface IcbcCardRepository extends JpaRepository<IcbcCard,Long> {
	IcbcCard findByIdAndCardId(Long id,String cardId);
	List<IcbcCard> findByCardId(String account,Pageable pageable) ;
	List<IcbcCard> findByUsername(String username,Pageable pageable) ; 
}
