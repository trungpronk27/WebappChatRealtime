package com.messages.controller;

import com.messages.entity.*;
import com.messages.repository.GroupChatRepository;
import com.messages.repository.GroupUserRepository;
import com.messages.repository.MessGroupRepository;
import com.messages.repository.UserRepository;
import com.messages.service.GroupChatService;
import com.messages.service.UserService;
import com.messages.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/group/")
public class GroupChatController {


    @Autowired
    private GroupChatService groupChatService;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private MessGroupRepository messGroupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @GetMapping("/group") // list group
    public String homeGroupChat(@AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl, Model model)
    {
//        model.addAttribute("listGroup",groupChatService.getListGroupChat(userDetailServiceImpl.getId()));

        model.addAttribute("listGroup",groupChatService.getListGroupChat(userDetailServiceImpl.getId()));
        model.addAttribute("listGroupSorted",groupChatRepository.getGroupByTimeChat(userDetailServiceImpl.getId()));
        return "groupChats/groupChat";
    }

    @PostMapping("/group")
    public String searchGroup(String key, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl , Model model)
    {
        model.addAttribute("listGroup",groupChatRepository.searchGroup(key, userDetailServiceImpl.getId()));
        return "groupChats/groupChat";
    }

    @GetMapping("/group/{id}/mess") // view group chat
    public String getMessByIdGroupChat(@PathVariable(value = "id") Integer id, Model model, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl) {
        String check = groupChatRepository.checkGroup(id, userDetailServiceImpl.getId());
        if(check != null){
//            model.addAttribute("listGroup",groupChatService.getListGroupChat(userDetailServiceImpl.getId()));

            model.addAttribute("listGroup",groupChatService.getListGroupChat(userDetailServiceImpl.getId()));
            model.addAttribute("listGroupSorted",groupChatRepository.getGroupByTimeChat(userDetailServiceImpl.getId()));

            model.addAttribute("contentMess",groupChatService.getMessGroupChat(id));
            model.addAttribute("group",groupChatRepository.findById(id).get());
            model.addAttribute("updateGroup", groupChatRepository.findById(id)); // update Group
            model.addAttribute("groupUser", groupUserRepository.getUserByIdGroup(id)); // show user Group
            return "groupChats/groupChatConversation";
        }else{
            return "redirect:/group";
        }
    }

    @GetMapping("/group/loadLastMessageInGroup")
    @ResponseBody
    public MessGroup loadLastMessageInGroup(@Param("cvt") int cvt){
        return messGroupRepository.loadLastMessageInGroup(cvt);
    }

    @PostMapping("/group/add") // created group
    public String addGroup(@ModelAttribute("group") Group group, GroupUser groupUser, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl)  throws IOException
    {
        group.setGroupImg("default.jpg");
        groupChatRepository.save(group); // add group

        // add user_id và group_id vào group user
        groupUser.setGroup_id(group);
        User user = new User();
        user.setId(userDetailServiceImpl.getId());
        groupUser.setUser_id(user);
        groupUser.setIsAdmin(1);
        groupUserRepository.save(groupUser);

        return "redirect:/group";
    }

    @PostMapping("/group/update/{id}") // update name group
    public String updateGroup(@PathVariable(value = "id") Integer id, @ModelAttribute("updateGroup") Group group, GroupUser groupUser)
    {
        group.setId(id);
        groupChatRepository.save(group);
        return "redirect:/group/{id}/mess";
    }

    @PostMapping("/group/upload/{id}") // upload img group
    public String uploadGroup(@PathVariable(value = "id") Integer id,  @RequestParam("image") MultipartFile multipartFile, @ModelAttribute("updateGroup") Group group )  throws IOException
    {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("er");
        }
        group.setId(id);
        group.setGroupImg(fileName);
        groupChatRepository.save(group);
        return "redirect:/group/{id}/mess";
    }

    @GetMapping("/group/delete/member/{id}") // delete member
    @ResponseBody
    public void deleteMember(@PathVariable(value = "id") Integer id)
    {
        groupUserRepository.deleteById(id);
    }

    @MessageMapping("/groups/{id}")
    @SendTo("/topic/group/{id}")
    public MessGroup message(@DestinationVariable(value = "id") Integer id, MessGroup messGroup)
    {
        User user = new User();
        user.setId(messGroup.getId_user_send());

        Group group = new Group();
        group.setId(id);

        Date date = new Date();

        // format date để check trường startime trong database
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);

        // kiểm tra đã tồn tại tin nhắn khởi đầu trong group
        Optional<MessGroup> check = messGroupRepository.checkStartTime(id, strDate); // check xem đã có startime chưa

        if(!check.isPresent()){ // nếu chưa có tin nhắn đầu tiên thì nhập vào
            messGroup.setContent(messGroup.getContent());
            messGroup.setTimeSend(date);
            messGroup.setStartTime(date);
            messGroup.setGroup_id(group);
            messGroup.setGroup_user_send(user);
            messGroupRepository.save(messGroup);
        }else {
            // format dữ date startime để check với date ngày hiện tại
            //Optional<MessGroup> messGroup1 = messGroupRepository.checkStartTime(id, strDate); // check xem đã có startime chưa
            String startime = formatter.format(check.get().getStartTime());

            messGroup.setContent(messGroup.getContent());
            messGroup.setTimeSend(date);
            messGroup.setGroup_id(group);
            messGroup.setGroup_user_send(user);
            //nếu starttime khác ngày hiện tại thì lưu
            if(startime.compareTo(strDate) < 0){
                messGroup.setStartTime(date);
            }
            messGroupRepository.save(messGroup);
        }
        return messGroup;
    }

    @GetMapping("/group/add/{id}")
    public String addMem(@PathVariable(value = "id") Integer id, Model model, @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl) {
        model.addAttribute("group",groupChatRepository.findById(id).get());
        return "groupChats/groupAddMember";
    }

    @PostMapping("/group/add/{id}")  // search member add
    public String search(@PathVariable(value = "id") Integer id, String key,   @AuthenticationPrincipal UserDetailServiceImpl userDetailServiceImpl, Model model) {
        model.addAttribute("group",groupChatRepository.findById(id).get());
        model.addAttribute("list", userRepository.search(userDetailServiceImpl.getId(),key));
        model.addAttribute("add", new GroupUser());
        return "groupChats/groupAddMember";
    }

    @PostMapping("/group/add/member/{id}/{userId}") // add member
    public String addMember(@PathVariable(value = "id") Integer id, @PathVariable(value = "userId") Integer userId, Model model) {
        Date date = new Date();
        String check = groupUserRepository.check(id, userId);
        if(check != null){
            model.addAttribute("isExist", "User is exist");
        }else{
            groupUserRepository.saveUser(id, userId, 0, date, date );
            model.addAttribute("success", "Successful add member");
        }
        return "redirect:/group/add/{id}";
    }
}