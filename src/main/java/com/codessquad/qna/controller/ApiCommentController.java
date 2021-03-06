package com.codessquad.qna.controller;

import com.codessquad.qna.entity.Comment;
import com.codessquad.qna.entity.User;
import com.codessquad.qna.service.CommentService;
import com.codessquad.qna.util.HttpSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class ApiCommentController {

    private static final Logger logger = LoggerFactory.getLogger(ApiCommentController.class);

    private final CommentService commentService;

    public ApiCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 세션에 로그인 되어 있어야만 작성 가능하며 로그인 되어 있지 않을시 NotExistLoggedUserInSession 발생
     * 로그인 되어 있다면 정상적으로 댓글 작성 가능함
     * 정상적인 추가가 이루어 졌다면 Comment 객체를 리턴한다.
     *
     * @param postId      Post Id
     * @param body        댓글 내용
     * @param httpSession
     * @return Comment Instance
     */
    @PostMapping("")
    public Comment addComment(@PathVariable Long postId, String body, HttpSession httpSession) {
        return commentService.addComment(postId, HttpSessionUtils.getUserFromSession(httpSession), body);
    }

    /**
     * 댓글 업데이트 창으로 이동합니다.
     * 세션에 로그인 된 유저와 댓글의 작성자가 다르다면 IllegalAccessException 이 발생합니다.
     *
     * @param postId      댓글이 종속되어 있는 post의 Id 입니다.
     * @param id          댓글의 id 입니다.
     * @param httpSession
     * @param model
     * @return
     * @throws IllegalAccessException
     */
    @GetMapping("/{id}")
    public String updateCommentForm(@PathVariable Long postId, @PathVariable Long id, HttpSession httpSession, Model model) throws IllegalAccessException {
        User sessionUser = HttpSessionUtils.getUserFromSession(httpSession);
        Comment comment = commentService.findComment(id);
        if (!comment.isMatchedUser(sessionUser)) {
            throw new IllegalAccessException("작성자만 댓글을 수정할 수 있습니다.");
        }
        model.addAttribute("postId", postId);
        model.addAttribute("comment", comment);
        return "qna/comment_update_form";
    }

    /**
     * Comment 를 입력한 값으로 update 합니다.
     *
     * @param postId 종속되어 있는 Post 의 Id
     * @param id
     * @param body   수정할 내용
     * @return
     */
    @PutMapping("/{id}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long id, String body) {
        Comment comment = commentService.findComment(id);
        commentService.updateComment(comment, body);
        return "redirect:/posts/" + postId;
    }

    /**
     * 해당 Id 에 해당 하는 게시물을 삭제합니다.
     * 세션에 로그인 되어 있는 유저와 댓글 작성자가 일치 하지 않을시 IllegalAccessException 이 발생합니다.
     * 정상적인 삭제 로직이 진행됬다면 true 를 리턴한다.
     *
     * @param postId      종속되어 있는 게시물의 Id
     * @param id          삭제할 댓글의 Id
     * @param httpSession
     * @return true
     * @throws IllegalAccessException
     */
    @DeleteMapping("/{id}")
    public boolean deleteComment(@PathVariable Long postId, @PathVariable Long id, HttpSession httpSession) throws IllegalAccessException {
        User sessionUser = HttpSessionUtils.getUserFromSession(httpSession);
        commentService.deleteComment(id, sessionUser);
        return true;
    }

}
