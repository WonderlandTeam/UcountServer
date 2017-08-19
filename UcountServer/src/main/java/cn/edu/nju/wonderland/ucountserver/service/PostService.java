package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostShareVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    /**
     * 根据筛选条件获取帖子
     * @param pageable      筛选信息
     * @return              帖子
     */
    List<PostInfoVO> getPosts(Pageable pageable);

    /**
     * 获取帖子信息
     * @param postId        帖子id
     * @return              帖子信息vo
     */
    PostInfoVO getPostInfo(Long postId);

    /**
     * 用户分享帖子
     * @param postShareVO   帖子发布信息vo
     * @return              帖子id
     */
    Long addPost(PostShareVO postShareVO);

    /**
     * 获取用户分享所有帖子
     * @param userId        用户id
     * @return              帖子信息列表
     */
    List<PostInfoVO> getPostsSharedByUser(Long userId);

    /**
     * 用户收藏帖子
     * @param userId        用户id
     * @param postId        帖子id
     */
    void collectPost(Long userId, Long postId);

    /**
     * 用户取消收藏
     * @param userId        用户id
     * @param postId        帖子id
     */
    void deleteCollection(Long userId, Long postId);

    /**
     * 获取用户收藏
     * @param userId        用户id
     * @return              帖子信息列表
     */
    List<PostInfoVO> getPostsCollectedByUser(Long userId);

    /**
     * 称赞帖子
     * @param userId        用户id
     * @param id            帖子id／回贴id
     * @param isReply       是否为回贴
     */
    void praisePost(Long userId, Long id, boolean isReply);

    /**
     * 取消称赞
     * @param userId        用户id
     * @param postId        帖子id／回贴id
     * @param isReply       是否为回贴
     */
    void cancelPraisePost(Long userId, Long postId, boolean isReply);

    /**
     * 回复帖子
     * @param postId        帖子id
     * @param postReplyVO   帖子回复信息vo
     */
    void replyPost(Long postId, PostReplyVO postReplyVO);

    /**
     * 获取帖子回复信息
     * @param postId        帖子id
     * @return              帖子回复信息列表
     */
    List<PostReplyVO> getPostReplies(Long postId);

}
