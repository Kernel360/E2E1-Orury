package com.kernel360.orury.domain.post.service;
import com.kernel360.orury.domain.post.PostViewRequest;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.dto.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;
import com.kernel360.orury.domain.post.repository.PostRepository;
import com.kernel360.orury.domain.reply.db.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostConverter postConverter;
    public PostDto createPost (
            PostRequest postRequest,
            Long userId,
            Long boardId
    ){
        var entity = PostEntity.builder()
                .postTitle(postRequest.getPostTitle())
                .postContent(postRequest.getPostContent())
                .userNickname(postRequest.getUserNickname())
                .userId(userId)
                .boardId(boardId)
                .build();
        var saveEntity = postRepository.save(entity);
        return postConverter.toDto(saveEntity);
    }
    public postDTo getPost (PostViewRequest postViewRequest){
        Long postId = postViewRequest.getId();
        Optional<PostEntity> postEntityOptional = postRepository.findByIdAndIsDelete(postId, 0);
        PostEntity post = postEntityOptional.orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + postId));
        return postConverter.toDto(post);
    }
    public void updatePost (
    ){
    }
    public void deletePost (
            Long postId
    ){
        postRepository.deleteById(postId);
    }
    public List<PostEntity> all() {
        return postRepository.findAll();
    }
}