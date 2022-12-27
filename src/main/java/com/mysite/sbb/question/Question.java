// 경로 : sbb/src/main/java/com/mysite/sbb/question/Question.java

package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Question {
	
	// 질문ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// 질문 제목
	@Column(length = 200)
	private String subject;
	
	
	// 질문 내용
	@Column(columnDefinition = "TEXT")
	private String content;
	
	// 질문 시간
	private LocalDateTime createDate;
	
	
	// 질문에 해당하는 답변
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	// 질문 글쓴이
	@ManyToOne
	private SiteUser author;
	
	// 수정 시간
	private LocalDateTime modifyDate;
	
	// 추천인
    @ManyToMany
    Set<SiteUser> voter;
}