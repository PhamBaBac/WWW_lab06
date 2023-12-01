package vn.edu.iuh.fit.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.models.Post;
import vn.edu.iuh.fit.models.PostComment;
import vn.edu.iuh.fit.repositories.PostCommentRepository;
import vn.edu.iuh.fit.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostCommentService {

    @Autowired
    private PostCommentRepository postCommentRepository;

    public List<PostComment> getAllPublishedComments() {
        return postCommentRepository.findByPublishedTrue();
    }
    public Optional<PostComment> getCommentById(Long id) {
        return postCommentRepository.findById(id);
    }
    public void save(PostComment postComment){ postCommentRepository.save(postComment);}

    public List<PostComment> getAllCommentsByPost(Post post) {
        return postCommentRepository.findByPost(post);
    }
    @Transactional
    public void deleteAllCommentsByPost(Post post) {
        postCommentRepository.deleteByPost(post);
    }
}
