package com.example.spring_ai_tutorial.controller

import com.example.spring_ai_tutorial.service.ChatService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Chat API 컨트롤러
 *
 * LLM API를 통해 채팅 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/v1/chat")
class ChatController(
    private val chatService: ChatService
) {
    private val logger = KotlinLogging.logger {}

    /**
     * 사용자의 메시지를 받아 OpenAI API로 응답 생성
     */

    @PostMapping("/query")
    suspend fun sendMessage(
        @Parameter(description = "채팅 요청 객체", required = true)
        @RequestBody request: ChatRequest
    ): ResponseEntity<ApiResponse<Map<String, Any>>> {
        logger.info { "Chat API 요청 받음: model=${request.model}" }

        // 유효성 검사
        if (request.query.isBlank()) {
            logger.warn { "빈 질의가 요청됨" }
            return ResponseEntity.badRequest().body(
                ApiResponse(success = false, error = "질의가 비어있습니다.")
            )
        }

        return try {
            // 시스템 프롬프트 지정
            val systemMessage = "You are a helpful AI assistant."

            // AI 응답 생성
            val response = chatService.openAiChat(
                userInput = request.query,
                systemMessage = systemMessage,
                model = request.model
            )
            logger.debug { "LLM 응답 생성: $response" }

            response?.let { chatResponse ->
                ResponseEntity.ok(
                    ApiResponse(
                        success = true,
                        data = mapOf("answer" to chatResponse.result.output.text)
                    )
                )
            } ?: run {
                logger.error { "LLM 응답 생성 실패" }
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse(
                        success = false,
                        error = "LLM 응답 생성 중 오류 발생"
                    )
                )
            }
        } catch (e: Exception) {
            logger.error(e) { "Chat API 처리 중 오류 발생" }
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse(
                    success = false,
                    error = e.message ?: "알 수 없는 오류 발생"
                )
            )
        }
    }
}

@Schema(description = "채팅 요청 데이터 모델")
data class ChatRequest(
    @Schema(description = "사용자 질문", example = "안녕하세요")
    val query: String,

    @Schema(description = "사용할 LLM 모델", example = "gpt-3.5-turbo", defaultValue = "gpt-3.5-turbo")
    val model: String = "gpt-3.5-turbo"
)

@Schema(description = "API 응답 포맷")
data class ApiResponse<T>(
    @Schema(description = "요청 처리 성공 여부")
    val success: Boolean,

    @Schema(description = "성공 응답 데이터")
    val data: T? = null,

    @Schema(description = "실패 오류 메시지")
    val error: String? = null
)
