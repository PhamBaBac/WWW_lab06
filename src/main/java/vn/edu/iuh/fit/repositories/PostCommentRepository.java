package vn.edu.iuh.fit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.models.Post;
import vn.edu.iuh.fit.models.PostComment;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPublishedTrue();
    Optional<PostComment> findById(Long id);
    List<PostComment> findByPost(Post post);
    void deleteByPost(Post post);
}