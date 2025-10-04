package com.staccato.category.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.staccato.ControllerTest;
import com.staccato.category.service.dto.response.CategoryDetailResponseV4;
import com.staccato.exception.ExceptionResponse;
import com.staccato.fixture.category.CategoryFixtures;
import com.staccato.fixture.member.MemberFixtures;
import com.staccato.member.domain.Member;
import com.staccato.category.domain.Category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerV4Test extends ControllerTest {

    @DisplayName("V4 카테고리 조회 요청/응답에 대한 직렬화/역직렬화에 성공한다.")
    @Test
    void readCategoryV4() throws Exception {
        // given
        Member member = MemberFixtures.ofDefault().withNickname("host").build();
        when(authService.extractFromToken(org.mockito.ArgumentMatchers.anyString())).thenReturn(member);

        Category category = CategoryFixtures.ofDefault()
                .withHost(member)
                .build();

        CategoryDetailResponseV4 response = new CategoryDetailResponseV4(category, member);
        when(categoryService.readCategoryById(anyLong(), any(Member.class))).thenReturn(response);

        // when & then
        mockMvc.perform(get("/v4/categories/{categoryId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // 주요 필드만 검증 (멤버 목록은 1명(host)만 구성)
                .andExpect(jsonPath("$.categoryId").doesNotExist())
                .andExpect(jsonPath("$.categoryTitle").value("categoryTitle"))
                .andExpect(jsonPath("$.categoryThumbnailUrl").value("https://example.com/categoryThumbnail.jpg"))
                .andExpect(jsonPath("$.isShared").value(false))
                .andExpect(jsonPath("$.members[0].nickname").value("nickname"));
    }

    @DisplayName("경로 변수(categoryId)가 양수가 아니면 400을 반환한다.")
    @Test
    void cannotReadCategoryV4ByInvalidId() throws Exception {
        // given
        Member member = MemberFixtures.ofDefault().build();
        when(authService.extractFromToken(org.mockito.ArgumentMatchers.anyString())).thenReturn(member);
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.toString(), "카테고리 식별자는 양수로 이루어져야 합니다.");

        // when & then
        mockMvc.perform(get("/v4/categories/{categoryId}", 0L)
                        .header(HttpHeaders.AUTHORIZATION, "token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(exceptionResponse), true));
    }
}