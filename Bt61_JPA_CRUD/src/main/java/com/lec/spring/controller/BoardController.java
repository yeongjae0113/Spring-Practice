package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.domain.PostValidator;
import com.lec.spring.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    public BoardController(){
        System.out.println("BoardController() 생성");
    }

    @GetMapping("/write")
    public void write(){}

    @PostMapping("/write")
    public String writeOk(@Valid Post post
            , BindingResult result
            , Model model   // 매개변수 선언시 BindingResult 보다 Model 을 뒤에 두어야 한다 !
            , RedirectAttributes redirectAttrs  // redirect 시 넘겨줄 값 들을 담는 객체
    ){

        // 만약 에러가 있다면 redirect 한다 !
        if(result.hasErrors()) {

            // addAttribute
            //    request parameters로 값을 전달.  redirect URL에 query string 으로 값이 담김
            //    request.getParameter에서 해당 값에 액세스 가능
            // addFlashAttribute
            //    일회성. 한번 사용하면 Redirect 후 값이 소멸
            //    request parameters 로 값을 전달하지않음
            //    '객체'로 값을 그대로 전달


            // redirect 시, 기존에 입력했던 값 들은 보이게 하기
            redirectAttrs.addFlashAttribute("user", post.getUser());
            redirectAttrs.addFlashAttribute("subject", post.getSubject());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
                // err.getCode() 가 err.getField() 로 간다.
            }

            return "redirect:/board/write";
        }

        model.addAttribute("result", boardService.write(post));
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){

        Post post = boardService.detail(id);
        model.addAttribute("post", post);

        return "board/detail";
    }

    @GetMapping("/list")
    public void list(Model model){
        model.addAttribute("list", boardService.list());    // 결과를 list 파일로
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("post", boardService.selectById(id));    // 선택한 id 를 post 로
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(@Valid Post post
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ){
        // 만약 에러가 있다면 redirect 한다 !
        if(result.hasErrors()) {
            redirectAttrs.addFlashAttribute("subject", post.getSubject());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());

            }

            return "redirect:/board/update/" + post.getId();
        }



        model.addAttribute("result", boardService.update(post));
        return "board/updateOk";    // 팝업을 띄워주기 위해
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model) {
        model.addAttribute("result", boardService.deleteById(id));
        return "board/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("BoardController.initBinder() 호출");
        binder.setValidator(new PostValidator());
    }


} // end controller