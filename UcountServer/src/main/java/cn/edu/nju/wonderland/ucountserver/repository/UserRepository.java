package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户信息（用户名，密码，手机号，邮箱）
 * Created by green-cherry on 2017/8/16.
 */
public interface UserRepository  extends JpaRepository<User,Long> {
	User findByUsername(String name);
}
