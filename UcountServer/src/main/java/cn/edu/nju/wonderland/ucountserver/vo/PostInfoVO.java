package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 帖子信息VO
 */
public class PostInfoVO {

    public Long postId;                 // 帖子id
    public String username;             // 用户名
    public String title;                // 标题
    public String content;              // 内容
    public String time;                 // 时间
    public int supportNum;              // 点赞数

    public PostInfoVO() {
    }

    public PostInfoVO(Long postId, String username, String title, String content, String time, int supportNum) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.time = time;
        this.supportNum = supportNum;
    }

}
