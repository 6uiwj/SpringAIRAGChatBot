package com.example.spring_ai_tutorial.service

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.google.genai.GoogleGenAiChatModel
import org.springframework.stereotype.Service


/**
 * OpenAI API를 사용하여 질의응답을 수행하는 서비스
 */
@Service
class ChatService(
    private val chatModel: GoogleGenAiChatModel
) {
    private val logger = KotlinLogging.logger {}

    /**
     * OpenAI 챗 API를 이용하여 응답을 생성합니다.
     *
     * @param userInput 사용자 입력 메시지
     * @param systemMessage 시스템 프롬프트
     * @param model 사용할 LLM 모델명
     * @return 챗 응답 객체, 오류 시 null
     */
    fun chat(
        userInput: String,
        systemMessage: String,
        model: String = "gemini-2.5-flash"
    ): ChatResponse? {
        return try {
            // 메시지 구성
            var messages = listOf(
                    SystemMessage(systemMessage),
                    UserMessage(userInput)
            )
            // 챗 옵션 설정
            val options = ChatOptions.builder()
                    .model(model)
                    .temperature(0.7)
                    .build()
            // 프롬프트 생성
            val prompt = Prompt(messages, options)
            chatModel.call(prompt)

        } catch (e: Exception) {
            logger.error(e) { "OpenAI 챗 호출 중 오류 발생: ${e.message}" }
            null
        }
    }
}