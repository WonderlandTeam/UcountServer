package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("SqlDialectInspection")
public interface TagRepository extends JpaRepository<Tag, String> {

    @Query(value =  "select name from ( " +
                        "select t.tag as name, count(*) as times from ( " +
                            "select post_id from collection where username=?1 " +
                            "union all " +
                            "select post_id from support where username=?1 and post_id is not null) p, " +
                            "post_tag t " +
                        "where p.post_id = t.post_id " +
                        "group by t.tag " +
                    ") temp " +
                    "order by times desc",
            nativeQuery = true)
    List<Tag> getFavouriteTags(String username);

}
