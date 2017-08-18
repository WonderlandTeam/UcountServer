package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *回复（用户名，回帖ID，帖子内容，时间，点赞数，原帖id）
 * Created by green-cherry on 2017/8/16.
 */
public interface ReplyRepository extends JpaRepository<Reply,Long> {
}
