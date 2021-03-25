package com.messages.controller;

import com.messages.config.FileUploadUtil;
import com.messages.entity.Comment;
import com.messages.entity.Friend;
import com.messages.entity.Post;
import com.messages.entity.User;
import com.messages.repository.CommentRepository;
import com.messages.repository.FriendRepository;
import com.messages.repository.PostRepository;
import com.messages.repository.UserRepository;
import com.messages.service.CommentService;
import com.messages.service.FriendService;
import com.messages.service.PostService;
import com.messages.service.UserService;
import com.messages.service.impl.UserDetailServiceImpl;
import com.messages.service.impl.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Value("${upload.path}")
    private String fileUpload;

    @GetMapping("/{id}") // profile user
    public String view(@PathVariable(value = "id") Integer id, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl, Model model )
    {
        Friend status = friendService.checkFriendStatus(id, userDetailServiceImpl.getId());

        model.addAttribute("count", friendService.countFriend(id));
        model.addAttribute("profile",  userService.getUserById(id));
        model.addAttribute("post", postService.getPostByIdUser(id));
        model.addAttribute("listFriend", friendRepository.listFriend(id));

        if(id.equals(userDetailServiceImpl.getId())){
            return "profile/profile";
        }else{
//            Friend check = friendService.checkUserBlock(id, userDetailServiceImpl.getId());
            if(status.getStatus() == 3){
                return "profile/block";
            }
            model.addAttribute("status", status);                                    // show status friend
            model.addAttribute("friend", new Friend());                              // add status friend
            model.addAttribute("update", friendRepository.findById(status.getId())); // update status friend
            return "profile/profileFriend";
        }
    }

    @GetMapping("/{id}/edit") // get id profile user edit
    public String edit(@PathVariable(value = "id") Integer id, Model model,  @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl)
    {
        if(id.equals(userDetailServiceImpl.getId())){
            model.addAttribute("edit", userRepository.findById(id));
            return "profile/edit";
        }else{
            return "404";
        }
    }

    @PostMapping("/{id}/edit") // update profile user
    public String save(Model model, @Valid @ModelAttribute("edit") User user, BindingResult errors){
        if (errors.hasErrors()){
            return "profile/edit";
        }
        try {
            userRepository.save(user);
            model.addAttribute("success", "Successful update");
        }catch (Exception e){
            model.addAttribute("isExist", "Email is exist");
            return "profile/edit";
        }
        return "profile/edit";
    }

    @PostMapping("/upload/{id}") // upload img user
    public RedirectView saveUser(@PathVariable(value = "id") Integer id, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("er");
        }
        userService.uploadUserImg(fileName, id);
        return new RedirectView("/profile/{id}", true);
    }

    @PostMapping("/createdPost/add/{id}") // add new post
        public String addNewPost(@PathVariable(value = "id") Integer id, @RequestParam("imagePost") MultipartFile multipartFile, @ModelAttribute("add") Post post) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "src/main/resources/static/img/posts/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        Date date = new Date();
        postService.savePost(post.getPostText(), fileName, id, date, date);
        return  "redirect:/profile/{id}";
    }

    @GetMapping("/comment/{id}") //
    @ResponseBody
    public List<Comment> getComment(@PathVariable(value = "id") Integer id, Model model)
    {
        model.addAttribute("success", commentService.getCommentByIdPost(id));
        return commentService.getCommentByIdPost(id);
    }

    @GetMapping ("/{id}/changePassword")
    public String changePassword(@PathVariable(value = "id") Integer id, Model model){
        model.addAttribute("change", userRepository.findById(id));
        return "profile/changePassword";
    }

    @PostMapping("/{id}/changePassword")
    public RedirectView savePassword(@PathVariable(value = "id") Integer id, HttpServletRequest request, Model model) {
        String oldPassword = request.getParameter("oldPassword");
        String password = request.getParameter("password");
        try {
            userService.changePassword(id, password, oldPassword);
            model.addAttribute("success", "Update successful!");
            return new RedirectView("/profile/{id}/changePassword", true);
        } catch (UserNotFoundException e) {
            model.addAttribute("error", "Old password is not precision");
            return new RedirectView("/profile/{id}/changePassword", true);
        }
    }
}
