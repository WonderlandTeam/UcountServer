package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;

/**
 * 回复（用户名，回帖ID，帖子内容，时间，点赞数，原帖id）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Reply {
    private String username;
    private Long replyId;
    private String content;
    private String time;
    private Post post;

    @ManyToOne
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name = "reply_id", nullable = false)
    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 100)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "time", nullable = true, length = 45)
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    

}
