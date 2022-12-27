// 경로 : sbb/src/main/java/com/mysite/sbb/question/QuestionController.java
package com.mysite.sbb.question;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	private final QuestionService questionService;
	private final UserService userService;
	
	// 질문 페이지 매핑
	@GetMapping("/question/list")
    public String list(Model model,
    		@RequestParam(value="page", defaultValue="0") int page,
    		@RequestParam(value="kw", defaultValue="") String kw) {
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }
	
	// 질문 상세 페이지 매핑
	@RequestMapping(value = "/question/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id,
			AnswerForm answerForm) {
		
		// 서비스를 통해 객체를 가져와 템플릿에 전달
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()") // 로그인 필요
	@GetMapping("/question/create")
	public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }
	
	
	// 질문 등록 저장 URL 매핑
	@PreAuthorize("isAuthenticated()") // 로그인 필요
	@PostMapping("/question/create")
	// 제목, 내용, 작성자를 파라미터로 받음
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }
	
	// 수정 URL매핑 (GET)
    @PreAuthorize("isAuthenticated()") // 로그인 필요
    @GetMapping("/question/modify/{id}")
    public String questionModify(QuestionForm questionForm,
    							 @PathVariable("id") Integer id, 
    							 Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        
        // 수정시 questionForm 리턴
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }
    
    // 수정 URL매핑 (POST)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm,
    							 BindingResult bindingResult, 
    							 Principal principal, 
    							 @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question,
        							questionForm.getSubject(),
        							questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
    
    // 삭제 URL 매핑 (GET)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/delete/{id}")
    public String questionDelete(Principal principal,
    							 @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        
        // 사용자 검사
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
    
    // 추천 URL 매핑 (GET)
    @PreAuthorize("isAuthenticated()") // 로그인한 사람만 추천 가능
    @GetMapping("/question/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        
        // 추천 중복검사
        if (question.getVoter().contains(siteUser) == true) {
        	this.questionService.votedel(question, siteUser);
        }
        else {
        	this.questionService.vote(question, siteUser);
        }
        return String.format("redirect:/question/detail/%s", id);
    }
    
}