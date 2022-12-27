// 경로 : sbb/src/main/java/com/mysite/sbb/question/QuestionService.java

package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 페이징 메서드
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    
    // id값으로 Question 데이터를 조회하는 메서드
    public Question getQuestion(Integer id) {
    	Optional<Question> question = this.questionRepository.findById(id);
    	if(question.isPresent()) {
    		return question.get();
    	}else {
    		throw new DataNotFoundException("question not found");
    	}
    }
    
    // 질문 저장하는 메서드
    public void create(String subject, String content, SiteUser user) {
    	Question q = new Question();
    	q.setSubject(subject);
    	q.setContent(content);
    	q.setCreateDate(LocalDateTime.now());
    	q.setAuthor(user);
    	this.questionRepository.save(q);
    }
    
    // 질문 수정하는 메서드
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    
    // 질문 삭제 메서드
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
    
    // 질문 추천 메서드
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
    
    // 질문 추천 취소 메서드
    public void votedel(Question question, SiteUser siteUser) {
    	question.getVoter().remove(siteUser);
    	this.questionRepository.save(question);
    }
    

}