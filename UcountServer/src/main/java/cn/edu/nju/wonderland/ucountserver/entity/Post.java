package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * 理财经验（用户名，帖子ID，标题，帖子内容，时间）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Post {
    private String username;
    private Long postId;
    private String title;
    private String content;
    private Timestamp time;
    private List<Reply> replies;


    @Basic
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name = "post_id", nullable = false)
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }


    @Basic
    @Column(name = "title", nullable = false, length = 45)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }


    @OneToMany(mappedBy ="post")
    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (postId != post.postId) return false;
        if (username != null ? !username.equals(post.username) : post.username != null) return false;
        if (content != null ? !content.equals(post.content) : post.content != null) return false;
        if (time != null ? !time.equals(post.time) : post.time != null) return false;
        return replies != null ? replies.equals(post.replies) : post.replies == null;
    }


}
