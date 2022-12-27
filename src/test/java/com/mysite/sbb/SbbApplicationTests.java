// 경로 : sbb/src/test/java/com/mysite/sbb/SbbApplicationTests.java
package com.mysite.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.question.QuestionService;


@SpringBootTest
class SbbApplicationTests {
    
    @Autowired
    private QuestionService questionService;

    @Test
    void testJpa() {
    	for (int i = 1; i<= 300 ; i++) {
    		String subject = String.format("테스트 데이터 [%03d] ", i);
    		String content = "내용";
    		this.questionService.create(subject, content,null);
    	}
    	
    }
}