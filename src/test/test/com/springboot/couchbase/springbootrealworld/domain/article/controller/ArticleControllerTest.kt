package com.springboot.couchbase.springbootrealworld.domain.article.controller

import com.springboot.couchbase.springbootrealworld.SpringBootRealworldApplication
import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto
import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [SpringBootRealworldApplication::class])
@ActiveProfiles("test")
@DisplayName("Author Resource REST API Tests")
@Tag("IntegrationTest")
class ArticleControllerTest {

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    @DisplayName("when POST a new Author, then returns 201")
    fun givenNewAuthor_whenPostAuthor_thenReturns201() {
        // TODO: Add your test logic here
//        val request = HttpEntity(
//            ArticleDto.builder()
//                .title("mylife")
//                .description("iamtestingapp")
//                .body("itishard")
//                .tagList(listOf("tag1", "tag2"))
//                .build()
//        )

//        val responseEntity: ResponseEntity<Void> = restTemplate.postForEntity("/api/articles", request, Void::class.java)

//        assertEquals(CREATED, responseEntity.statusCode)
//        assertNotNull(responseEntity.headers.location)
    }
}
