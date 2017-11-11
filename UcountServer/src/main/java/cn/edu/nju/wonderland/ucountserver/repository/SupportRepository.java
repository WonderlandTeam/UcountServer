package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 点赞（用户名，帖子ID，回帖ID ）<-其中帖子ID和回帖ID是二选一的关系
 * Created by green-cherry on 2017/8/17.
 */
public interface SupportRepository  extends JpaRepository<Support, Long> {

    Support findByUsernameAndPostId(String username, Long postId);

    Support findByUsernameAndReplyId(String username, Long replyId);

    int countByPostId(Long postId);

    int countByReplyId(Long replyId);

    List<Support> findByUsernameAndPostIdNotNull(String username);

}
