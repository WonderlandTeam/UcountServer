package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 理财经验（用户名，帖子ID，标题，帖子内容，时间，点赞数）
 * Created by green-cherry on 2017/8/16.
 */
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByUsernameOrderByTimeDesc(String username);

//    @Query(value = "select distinct(tag) from post_tag where post_id=?1", nativeQuery = true)
//    List<String> getTagsByPostId(Long postId);

}
