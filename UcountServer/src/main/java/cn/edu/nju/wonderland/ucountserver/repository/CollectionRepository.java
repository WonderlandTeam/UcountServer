package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 收藏（用户名，帖子ID，收藏时间）
 * Created by green-cherry on 2017/8/16.
 */
public interface CollectionRepository extends JpaRepository<Collection,Long> {

    Collection findByUsernameAndPostId(String username, Long postId);

    List<Collection> findByUsernameOrderByColTimeDesc(String username);

    List<Collection> findByUsername(String username);

}
