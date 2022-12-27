// 경로 : sbb/src/main/java/com/mysite/sbb/answer/Answer.java

package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	
	// 답변 id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// 답변 내용
	@Column(columnDefinition = "TEXT")
	private String content;
	
	// 답변 시각
	private LocalDateTime createDate;
	
	// 질문
	@ManyToOne
	private Question question;
	
	@ManyToOne
	// 답변 글쓴이
	private SiteUser author;
	
	// 수정 시간
	private LocalDateTime modifyDate;
	
	// 추천인
    @ManyToMany
    Set<SiteUser> voter;
}
