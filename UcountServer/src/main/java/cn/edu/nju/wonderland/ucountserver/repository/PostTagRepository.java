package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Query("select distinct(tag) from PostTag where postId=?1")
    List<String> findTagsByPostId(Long postId);

}
