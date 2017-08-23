package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;

/**
 * 点赞（用户名，帖子ID，回帖ID ）<-其中帖子ID和回帖ID是二选一的关系
 * Created by green-cherry on 2017/8/17.
 */
@Entity
@Table(name = "support", schema = "Ucount_data", catalog = "")
public class Support {
    private Long id;
    private String username;
    private Long postId;
    private Long replyId;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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
    @Column(name = "post_id", nullable = true)
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "reply_id", nullable = true)
    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Support that = (Support) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
        if (replyId != null ? !replyId.equals(that.replyId) : that.replyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        result = 31 * result + (replyId != null ? replyId.hashCode() : 0);
        return result;
    }
}
