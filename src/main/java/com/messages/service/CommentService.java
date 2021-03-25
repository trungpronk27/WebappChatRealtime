package com.messages.service;

import com.messages.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentByIdPost(Integer idPost);
}
