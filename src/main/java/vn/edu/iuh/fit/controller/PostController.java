package vn.edu.iuh.fit.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.iuh.fit.models.Post;
import vn.edu.iuh.fit.models.PostComment;
import vn.edu.iuh.fit.models.User;
import vn.edu.iuh.fit.service.PostCommentService;
import vn.edu.iuh.fit.service.PostService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("user")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostCommentService postCommentService;
    @GetMapping("/posts")
    public String getAllPublishedPosts(Model model) {
        List<Post> posts = postService.getAllPublishedPosts();
        model.addAttribute("posts", posts);
        return "post/listPost";
    }
    @GetMapping("/posts/{id}")
    public String getPostDetails(@PathVariable("id") Long id, Model model) {
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            List<PostComment> comments = postCommentService.getAllCommentsByPost(post);
            model.addAttribute("comments", comments);
            model.addAttribute("post", post);
            return "post/detailPost";
        } else {
            return "error";
        }
    }
    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable("id") Long id, @RequestParam("comment") String comment, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        PostComment newComment = new PostComment();
        newComment.setContent(comment);
        newComment.setTitle("Your title here");
        newComment.setUser(sessionUser);
        newComment.setPublished(true);
        newComment.setCreatedAt(Instant.now());
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            newComment.setPost(post);
            post.getPostComments().add(newComment);
            postCommentService.save(newComment);
            model.addAttribute("post", post);
        } else {
            return "error";
        }
        return "redirect:/posts/" + id;
    }

    @GetMapping("/posts/new")
    public String showCreatePostForm(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "post/newPost";
    }
    @PostMapping("/posts/new")
    public String createPost(@ModelAttribute("post") Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        post.setPublished(true);
        post.setCreatedAt(Instant.now());
        post.setPublishedAt(Instant.now());
        post.setAuthor(user);
        postService.savePost(post);
        return "redirect:/posts";
    }


    @PostMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getAuthor().getEmail().equals(user.getEmail())) {
                postCommentService.deleteAllCommentsByPost(post);
                postService.deletePost(post);
                return "redirect:/posts";
            } else {
                redirectAttributes.addFlashAttribute("error", "Bạn không thể xóa bài đăng này vì bạn không phải là tác giả.");
                return "redirect:/posts/" + id;
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/posts/update/{id}")
    public String getUpdatePost(@PathVariable("id") Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getAuthor().getEmail().equals(user.getEmail())) {
                model.addAttribute("post", post);
                return "post/updatePost";
            } else {
                model.addAttribute("error", "Bạn không thể cập nhật bài đăng này vì bạn không phải là tác giả.");
                return "redirect:/posts/" + id;
            }
        } else {
            return "error";
        }
    }
    @PostMapping("/posts/update/{id}")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute("post") Post post, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();
            if (existingPost.getAuthor().getEmail().equals(user.getEmail())) {
                existingPost.setUpdatedAt(Instant.now());
                existingPost.setMetaTitle(post.getMetaTitle());
                existingPost.setContent(post.getContent());
                postService.savePost(existingPost);
                return "redirect:/posts/" + id;
            } else {
                redirectAttributes.addFlashAttribute("error", "Bạn không thể cập nhật bài đăng này vì bạn không phải là tác giả.");
                return "redirect:/posts/" + id;
            }
        } else {
            return "error";
        }
    }
}
