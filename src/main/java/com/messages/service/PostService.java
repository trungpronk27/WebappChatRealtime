package com.messages.service;

import com.messages.entity.Employee;
import com.messages.entity.Post;

import java.util.Date;
import java.util.List;

public interface PostService {
    List<Post> getPostByIdUser(Integer userId);
    void savePost(String text , String img, Integer userId, Date cr, Date up);

}
