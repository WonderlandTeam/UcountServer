package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.repository.CollectionRepository;
import cn.edu.nju.wonderland.ucountserver.repository.PostRepository;
import cn.edu.nju.wonderland.ucountserver.repository.ReplyRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SupportRepository;
import cn.edu.nju.wonderland.ucountserver.service.PostService;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostShareVO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final SupportRepository supportRepository;
    private final CollectionRepository collectionRepository;

    public PostServiceImpl(PostRepository postRepository, ReplyRepository replyRepository, SupportRepository supportRepository, CollectionRepository collectionRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.supportRepository = supportRepository;
        this.collectionRepository = collectionRepository;
    }


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
    public void collectPost(Long userId, Long postId) {

    }

    @Override
    public void deleteCollection(Long userId, Long postId) {

    }

    @Override
    public List<PostInfoVO> getPostsCollectedByUser(Long userId) {
        return null;
    }

    @Override
    public void praisePost(Long userId, Long id, boolean isReply) {

    }

    @Override
    public void cancelPraisePost(Long userId, Long postId, boolean isReply) {

    }

    @Override
    public void replyPost(Long postId, PostReplyVO postReplyVO) {

    }

    @Override
    public List<PostReplyVO> getPostReplies(Long postId) {
        return null;
    }
}
