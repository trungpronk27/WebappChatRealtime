package com.messages.service.impl;

import com.messages.entity.Post;
import com.messages.repository.PostRepository;
import com.messages.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired //inject bean
    private PostRepository postRepository;

    @Override
    public List<Post> getPostByIdUser(Integer userId) {
        return postRepository.getPost(userId);
    }

    @Override
    public void savePost(String text , String img, Integer userId, Date cr, Date up) {
        postRepository.savePosts(text, img, userId, cr, up);
    }
}
