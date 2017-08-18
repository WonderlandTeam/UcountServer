package cn.edu.nju.wonderland.ucountserver.vo;

import cn.edu.nju.wonderland.ucountserver.entity.Post;

import java.util.List;

/**
 * 帖子信息VO
 */
public class PostInfoVO {

    public String username;             // 用户名
    public String title;                // 标题
    public String content;              // 内容
    public String time;                 // 时间
    public int supportNum;           // 点赞数
    public List<PostReplyVO> replies;   // 回复（按赞数、时间排序初始10条）

    public PostInfoVO() {
    }

    public PostInfoVO(Post entity) {
        this.username = entity.getUsername();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.time = entity.getTime();
        this.supportNum = entity.getSupportNum();
    }

    public PostInfoVO(String username, String title, String content, String time, int supportNum, List<PostReplyVO> replies) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.time = time;
        this.supportNum = supportNum;
        this.replies = replies;
    }

}
