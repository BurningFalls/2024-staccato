package com.staccato.comment.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 수정 시 요청 형식입니다.")
public record CommentUpdateRequest(
        @Schema(example = "예시 수정된 댓글 내용")
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        @Size(max = 500, message = "댓글은 공백 포함 500자 이하로 입력해주세요.")
        String content
) {
}
