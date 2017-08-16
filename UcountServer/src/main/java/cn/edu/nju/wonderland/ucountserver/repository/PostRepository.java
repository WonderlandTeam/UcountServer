package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by green-cherry on 2017/8/16.
 */
public interface PostRepository extends JpaRepository<Post,Long> {
}
