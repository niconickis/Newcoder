package com.nowcoder;

import com.nowcoder.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WendaApplication.class, args);
	}
}

// 最后一点：
// ****************************
// 终于知道知道Lession 7死活没有对request的响应了，我换了Lession 8中的全部静态文件（resources）到Lession 7中发现可以了
// *****************************
