package com.chanceToMe.MoonGuWanGu;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chanceToMe.MoonGuWanGu.controller.MemberController;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MoonGuWanGuApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  MemberController memberController;

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
  class signup {

    @Test
    @DisplayName("정상 요청에 대해 201 응답")
    void idealRequest() throws Exception {
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("email", "test@gmail.com");

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/member").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("중복 Email에 대해 400 응답")
    void invalidEamil() throws Exception {
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("email", "invalid email");

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/member").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("중복 Email에 대해 400 응답")
    void duplicatedEmail() throws Exception {
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("email", "duplicated@test.com");

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/member").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("POST /api/metadata")
  class AddMetaDataTest {

    @Test
    @DisplayName("정상 요청에 대해 201 응답")
    void idealRequest() throws Exception {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("imageUrl", "test_image_url");
      requestBody.put("grade", 0);
      requestBody.put("category", "test_category");

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/metadata").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("imageUrl 누락에 대해 400 응답")
    void missingImageUrl() throws Exception {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("grade", 0);
      requestBody.put("category", "test_category");

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/metadata").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("grade 누락에 대해 400 응답")
    void missingGrade() throws Exception {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("imageUrl", "test_image_url");
      requestBody.put("category", "test_category");

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/metadata").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("category 누락에 대해 400 응답")
    void missingCategory() throws Exception {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("imageUrl", "test_image_url");
      requestBody.put("grade", 0);

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/metadata").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("5를 초과하는 grade에 대해 400 응답")
    void gradeOverFive() throws Exception {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("imageUrl", "test_image_url");
      requestBody.put("grade", 100);
      requestBody.put("category", "test_category");

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/metadata").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("GET /api/metadata")
  class FindMetaDataTest {

    @Test
    @DisplayName("정상 요청에 대해 MetaData 리스트와 200 응답")
    void idealRequest() throws Exception {
      mockMvc.perform(
                 MockMvcRequestBuilders.get("/api/metadata?category=category")
                                       .contentType(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath(".list").isArray())
             .andExpect(status().isOk());
    }


    @Test
    @DisplayName("항목이 하나도 존재하지 않으면 404 응답")
    void nonExistedCategory() throws Exception {
      mockMvc.perform(
                 MockMvcRequestBuilders.get("/api/metadata?category=non_existed_category")
                                       .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("POST /api/card")
  class DrawCardTest {

    String testUserId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

    @Test
    @DisplayName("정상 요청에 대해 201 응답")
    void idealRequest() throws Exception {
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("memberId", testUserId);

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/card").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("존재하지 않는 Member일 경우 404 응답")
    void nonExistedMember() throws Exception {
      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("memberId", UUID.randomUUID().toString());

      mockMvc.perform(
                 MockMvcRequestBuilders.post("/api/card").contentType(MediaType.APPLICATION_JSON)
                                       .content(String.valueOf(new JSONObject(requestBody))))
             .andExpect(status().isNotFound());
    }
  }
}
