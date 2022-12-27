// 경로 : sbb/src/main/java/com/mysite/sbb/visual/VisualizationController.java

package com.mysite.sbb.visual;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VisualizationController {
	
	
	// 시각화 페이지1 매핑
	@RequestMapping("/vis1")
	public String vis_1() {
		return "vis1";
	}
	
	// 시각화 페이지2 매핑
	@RequestMapping("/vis2")
	public String vis_2() {
		return "vis2";
	}
}
