package ru.otus.hw.controller.mvc;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MainController {


    @GetMapping("/")
    public String front() {
        return "index";
    }

    @GetMapping("/list")
    public String getBooksPage() {
        return "list";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable("id") Long id, Model model){
        return "edit";
    }

    @GetMapping("/save")
    public String getSavePage(){
        return "save";
    }

    @GetMapping("/author")
    public String getAuthorSavePage(){
        return "author";
    }

    @GetMapping("/genre")
    public String getGenreSavePage(){
        return "genre";
    }

    @GetMapping("comment-list/{id}")
    public String getAllCommentsPage(@PathVariable("id") Long bookId){
        return "comment-list";
    }

    @GetMapping("/comment")
    public String getCommentSavePage(){
        return "comment";
    }
}
