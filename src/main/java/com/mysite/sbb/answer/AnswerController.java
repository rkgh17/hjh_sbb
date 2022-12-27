// 경로 : sbb/src/main/java/com/mysite/sbb/answer/AnswerController.java
package com.mysite.sbb.answer;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/answer") // URL 프리픽스
@RequiredArgsConstructor
@Controller
public class AnswerController {

    // 변수 지정
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    // 답변 (POST) 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}") // post요청만 받아들일 경우에 사용하는 에너테이션. (value=) 생략가능
    public String createAnswer(Model model, @PathVariable("id") Integer id,
    						   @Valid AnswerForm answerForm, 
    						   BindingResult bindingResult, 
    						   Principal principal) {
        Question question = this.questionService.getQuestion(id);
        
        // 글쓴이 속성
        SiteUser siteUser = this.userService.getUser(principal.getName());
        
    	// 검증 실패시 다시 리턴
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        
        // 답변저장 - 답변 객체
        Answer answer = this.answerService.create(question, 
        		answerForm.getContent(), 
        		siteUser);
        
        // 답변 작성 후 앵커로 돌아감
        return String.format("redirect:/question/detail/%s#answer_%s", 
        		answer.getQuestion().getId(), 
        		answer.getId());
    }
    
    // 답변 수정 (GET) 처리
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify( AnswerForm answerForm, 
    							@PathVariable("id") Integer id, 
    							Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }
    
    // 답변 수정 (POST) 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(
    		@Valid AnswerForm answerForm,
    		BindingResult bindingResult,
            @PathVariable("id") Integer id,
            Principal principal) {
    	
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        
        // 답변 완료 후 앵커로 돌아감
        return String.format("redirect:/question/detail/%s#answer_%s",
        		answer.getQuestion().getId(), 
        		answer.getId());
    }
    
    // 답변 삭제 (GET)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(
    		Principal principal,
    		@PathVariable("id") Integer id) {
    	
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
    
    // 추천 URL 매핑 (GET)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        
        if(answer.getVoter().contains(siteUser) == true) {
        	this.answerService.votedel(answer, siteUser);
        }
        else {
        	this.answerService.vote(answer, siteUser);
        }
        
        // 답변 추천 후 앵커로 돌아감
        return String.format("redirect:/question/detail/%s#answer_%s", 
        		answer.getQuestion().getId(), 
        		answer.getId());
    }

}