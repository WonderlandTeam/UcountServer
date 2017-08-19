package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 收藏经验（用户名，帖子ID，收藏时间）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private Long postId;
    private Timestamp colTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "post_id", nullable = false)
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "col_time", nullable = true)
    public Timestamp getColTime() {
        return colTime;
    }

    public void setColTime(Timestamp colTime) {
        this.colTime = colTime;
    }

}
