package com.example.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Memerlukan database MySQL aktif di localhost:3306")
@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
