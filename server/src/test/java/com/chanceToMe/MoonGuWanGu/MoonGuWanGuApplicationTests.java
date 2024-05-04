package com.chanceToMe.MoonGuWanGu;

import com.chanceToMe.MoonGuWanGu.controller.MainController;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MoonGuWanGuApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	MainController mainController;

	@Autowired
	DataSource dataSource;

	@BeforeAll
	public void beforeAll() throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/integration.test.ddl.sql"));
		}
	}

	@AfterAll
	public void afterAll() throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/cleandb.test.ddl.sql"));
		}
	}

	@Nested
	@DisplayName("POST /api/join")
	class signup{

		@Test
		@DisplayName("정상 요청에 대해 201 응답")
		void idealRequest () throws Exception {
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("email", "test@gmail.com");

			mockMvc.perform(MockMvcRequestBuilders
							.post("/api/join")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(requestBody))))
					.andExpect(status().isCreated());
		}

		@Test
		@DisplayName("중복 Email에 대해 400 응답")
		void invalidEamil() throws Exception {
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("email", "invalid email");

			mockMvc.perform(MockMvcRequestBuilders
							.post("/api/join")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(requestBody))))
					.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("중복 Email에 대해 400 응답")
		void duplicatedEmail() throws Exception {
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("email", "duplicated@test.com");

			mockMvc.perform(MockMvcRequestBuilders
							.post("/api/join")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(requestBody))))
					.andExpect(status().isBadRequest());
		}

	}
}
