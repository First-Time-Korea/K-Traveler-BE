package com.ssafy.firskorea.attraction.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//프롬프트 요청 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompletionRequestDto {

    private String model;
    private List<Message> messages;

    @Builder
    public CompletionRequestDto(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Message {
        private String role;
        private String content;
        private Float temperature;

        @Builder
        public Message(String role, String content, Float temperature) {
            this.role = role;
            this.content = content;
            this.temperature= temperature;
        }
    }
}