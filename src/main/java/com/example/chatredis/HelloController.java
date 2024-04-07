package com.example.chatredis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "/index.html";
    }

    @GetMapping("/chatRoomList")
    public String chatRoomList() {
        return "/chatRoomList.html";
    }

    @GetMapping("/groupChatRoomDetails/{roomId}")
    public String groupChatRoomDetails(@PathVariable("roomId") Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "/groupChatRoomDetails.html";
    }

    @GetMapping("/privateChatRoomDetails/{roomId}")
    public String privateChatRoomDetails(@PathVariable("roomId") Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "/privateChatRoomDetails.html";
    }

    @GetMapping("/chat")
    public String chat() {
        return "redirect:/room";
    }

}