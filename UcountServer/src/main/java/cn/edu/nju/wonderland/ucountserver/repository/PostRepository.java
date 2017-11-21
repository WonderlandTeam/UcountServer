package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 理财经验（用户名，帖子ID，标题，帖子内容，时间，点赞数）
 * Created by green-cherry on 2017/8/16.
 */
@SuppressWarnings("SqlDialectInspection")
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByUsernameOrderByTimeDesc(String username);

//    @Query(value = "select distinct(tag) from post_tag where post_id=?1", nativeQuery = true)
//    List<String> getTagsByPostId(Long postId);

    @Query(value = "SELECT p.* FROM post p, post_tag t WHERE p.post_id = t.post_id AND t.tag=?1 limit ?2", nativeQuery = true)
    List<Post> getPostsByTagAndLimit(String tag, int limit);

    @Query(value = "SELECT p.* FROM post p, post_tag t WHERE p.post_id = t.post_id AND t.tag=?1", nativeQuery = true)
    List<Post> getPostsByTag(String tag);

}
