package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;

@Entity
public class PostTag {
    private Long id;
    private Long postId;
    private String tag;

    public PostTag() {}

    public PostTag(Long postId, String tag) {
        this.postId = postId;
        this.tag = tag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "post_id", nullable = false)
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
