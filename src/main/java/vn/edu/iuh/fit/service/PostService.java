package vn.edu.iuh.fit.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.models.Post;
import vn.edu.iuh.fit.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPublishedPosts() {
        return postRepository.findByPublishedTrue();
    }
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    public Post savePost(Post post) {
        return postRepository.save(post);
    }
    public void deletePost(Post post) {
        postRepository.delete(post);
    }
}
