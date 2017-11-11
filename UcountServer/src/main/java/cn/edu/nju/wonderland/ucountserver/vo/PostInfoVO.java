package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 帖子信息VO
 */
public class PostInfoVO {

    public Long postId;                 // 帖子id
    public String username;             // 用户名
    public String title;                // 标题
    public String content;              // 内容
    private String time;                // 时间
    public int supportNum;              // 点赞数
    public boolean isCollected;         // 用户是否收藏
    public boolean isSupported;         // 用户是否点赞

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = "'"+time+"'";
    }
}
