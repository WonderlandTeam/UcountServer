package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 帖子回复信息VO
 */
public class PostReplyVO {
    public String username;
    public String content;
    public String time;
    public int supportNum;

    public PostReplyVO() {
    }

    public PostReplyVO(String username, String content, String time, int supportNum) {
        this.username = username;
        this.content = content;
        this.time = time;
        this.supportNum = supportNum;
    }

}
