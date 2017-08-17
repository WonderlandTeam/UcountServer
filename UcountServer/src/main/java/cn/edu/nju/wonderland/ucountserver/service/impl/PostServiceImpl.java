package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.service.PostService;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostShareVO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Override
    public List<PostInfoVO> getPosts(Pageable pageable) {
        return null;
    }

    @Override
    public PostInfoVO getPostInfo(Long postId) {
        return null;
    }

    @Override
    public Long addPost(PostShareVO postShareVO) {
        return null;
    }

    @Override
    public List<PostInfoVO> getPostsSharedByUser(Long userId) {
        return null;
    }

    @Override
    public List<PostInfoVO> getPostsCollectedByUser(Long userId) {
        return null;
    }

    @Override
    public void praisePost(Long postId) {

    }

    @Override
    public void replyPost(Long postId, PostReplyVO postReplyVO) {

    }

    @Override
    public List<PostReplyVO> getPostReplies(Long postId) {
        return null;
    }
}
