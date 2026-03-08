package com.example.spring_ai_tutorial.service


import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.stereotype.Service

@Service
class EmbeddingService(
        private val embeddingModel: EmbeddingModel
) {
        fun embed(text: String): FloatArray {
            return embeddingModel.embed(text)
        }
}