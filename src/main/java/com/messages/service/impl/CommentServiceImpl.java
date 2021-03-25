package com.messages.service.impl;

import com.messages.entity.Comment;
import com.messages.repository.CommentRepository;
import com.messages.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentByIdPost(Integer idPost) {
        return commentRepository.getComment(idPost);
    }
}
