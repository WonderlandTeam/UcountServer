package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



/**
 * 账户信息（用户名，卡类型，账号）
 * Created by green-cherry on 2017/8/16.
 */

public interface AccountRepository extends JpaRepository<Account,Long> {
	Account findById(Long id);

	Account findByUsernameAndCardTypeAndCardId(String username, String cardType, String cardId);

	List<Account> findByUsername(String username);

}
