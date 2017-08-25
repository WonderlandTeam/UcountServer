package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.PostAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    /**
     * 根据筛选条件获取帖子
     * @param pageable          筛选信息
     * @param username          查看帖子用户
     * @return                  帖子列表
     */
    Page<PostInfoVO> getPosts(Pageable pageable, String username);

    /**
     * 获取帖子信息
     * @param postId            帖子id
     * @param username          查看帖子用户名
     * @return                  帖子信息vo
     */
    PostInfoVO getPostInfo(Long postId, String username);

    /**
     * 用户分享帖子
     * @param postAddVO         帖子发布信息vo
     * @return                  帖子id
     */
    Long addPost(PostAddVO postAddVO);

    /**
     * 获取用户分享所有帖子
     * @param username          用户名
     * @return                  帖子信息列表
     */
    List<PostInfoVO> getPostsSharedByUser(String username);

    /**
     * 用户收藏帖子
     * @param username          用户名
     * @param postId            帖子id
     */
    void collectPost(String username, Long postId);

    /**
     * 用户取消收藏
     * @param username          用户名
     * @param postId            帖子id
     */
    void deleteCollection(String username, Long postId);

    /**
     * 获取用户收藏
     * @param username          用户名
     * @return                  帖子信息列表
     */
    List<PostInfoVO> getPostsCollectedByUser(String username);

    /**
     * 称赞帖子
     * @param username          用户名
     * @param id                帖子id／回贴id
     * @param isReply           是否为回贴
     */
    void praisePost(String username, Long id, boolean isReply);

    /**
     * 取消称赞
     * @param username          用户名
     * @param id                帖子id／回贴id
     * @param isReply           是否为回贴
     */
    void cancelPraisePost(String username, Long id, boolean isReply);

    /**
     * 获取帖子回复信息
     * @param replyId           帖子id
     * @return                  帖子回复信息vo
     */
    PostReplyVO getPostReplyInfo(Long replyId);

    /**
     * 回复帖子
     * @param postId            帖子id
     * @param postReplyAddVO    帖子回复vo
     * @return                  回贴id
     */
    Long replyPost(Long postId, PostReplyAddVO postReplyAddVO);

    /**
     * 获取帖子回复信息
     * @param postId            帖子id
     * @return                  帖子回复信息列表
     */
    List<PostReplyVO> getPostReplies(Long postId);

}
