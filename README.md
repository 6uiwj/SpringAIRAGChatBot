# 🤖 Spring AI RAG ChatBot

> Spring AI와 Google Gemini를 활용한 RAG(Retrieval-Augmented Generation) 챗봇 with Kotlin

---

## 📌 프로젝트 개요

PDF 문서를 업로드하면 해당 문서를 벡터 DB에 저장하고, 사용자 질문에 대해 관련 문서를 검색하여 Google Gemini LLM이 문서 기반 답변을 생성하는 RAG 챗봇입니다.

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin 1.9.25 |
| Framework | Spring Boot 3.4.4 |
| AI Framework | Spring AI 1.1.0 |
| LLM | Google Gemini (google-genai) |
| Embedding | Google GenAI Text Embedding (`gemini-embedding-001`) |
| Vector Store | SimpleVectorStore (In-Memory) |
| PDF 처리 | Apache PDFBox 2.0.30 |
| API 문서 | SpringDoc OpenAPI (Swagger) |
| Build Tool | Gradle (Kotlin DSL) |

---

## 📁 프로젝트 구조

```
src/main/kotlin/com/example/spring_ai_tutorial/
├── controller/
│   ├── RagController.kt        # PDF 업로드 및 RAG 질의 API
│   └── ChatController.kt       # 일반 챗봇 API
├── service/
│   ├── RagService.kt           # RAG 비즈니스 로직
│   ├── ChatService.kt          # Gemini 채팅 서비스
│   └── DocumentProcessingService.kt  # PDF 텍스트 추출
├── repository/
│   └── InMemoryDocumentVectorStore.kt  # 인메모리 벡터 스토어
├── config/
│   ├── VectorStoreConfig.kt    # SimpleVectorStore 빈 설정
│   └── SwaggerConfig.kt        # Swagger 설정
├── domain/dto/
│   ├── RagDto.kt               # RAG 관련 DTO
│   ├── DocumentDto.kt          # 문서 관련 DTO
│   └── CommonDto.kt            # 공통 응답 DTO
└── exception/
    └── DocumentProcessingException.kt
```

---

## ⚙️ 환경 설정

### 1. Google Gemini API 키 발급
[Google AI Studio](https://aistudio.google.com)에서 API 키를 발급받으세요.

### 2. 환경 변수 설정
```bash
export API_KEY=your_gemini_api_key
```

또는 IntelliJ 실행 설정의 Environment Variables에 `API_KEY=your_gemini_api_key` 추가


---



## 📡 API 명세

### 1. PDF 문서 업로드
```
POST /api/v1/reg/documents
Content-Type: multipart/form-data

file: (PDF 파일)
```

**응답**
```json
{
  "success": true,
  "data": {
    "documentId": "uuid",
    "message": "문서가 성공적으로 업로드되었습니다."
  }
}
```

### 2. RAG 질의
```
POST /api/v1/reg/query
Content-Type: application/json

{
  "query": "인공지능이란 무엇인가요?",
  "maxResults": 3,
  "model": "gemini-2.5-flash"
}
```

**응답**
```json
{
  "success": true,
  "data": {
    "query": "인공지능이란 무엇인가요?",
    "answer": "...(문서 기반 AI 답변)...\n\n참고 문서:\n[1] document.pdf",
    "relevantDocuments": [
      {
        "id": "uuid",
        "score": 0.92,
        "content": "...",
        "metadata": {}
      }
    ]
  }
}
```

---

## 🔄 RAG 동작 흐름

```
1. PDF 업로드
   └─> 텍스트 추출 (PDFBox)
       └─> 청크 분할 (TokenTextSplitter, 512 tokens)
           └─> 임베딩 변환 (Gemini Embedding)
               └─> 인메모리 벡터 스토어 저장

2. 질의 처리
   └─> 질문 임베딩 변환
       └─> 유사도 검색 (SimpleVectorStore)
           └─> 관련 문서 컨텍스트 구성
               └─> Gemini LLM 답변 생성
                   └─> 출처 정보 포함 응답 반환
```

---

## 실제 사용 예시
### 질문
![2026-03-16 18 36 40.png](images%2F2026-03-16%2018%2036%2040.png)

### 응답
- 유사도가 높은 5개의 청크를 뽑아 취합하여 AI가 답변 생성 
![2026-03-16 18 44 05.png](images%2F2026-03-16%2018%2044%2005.png)

### 실제 PDF 문서 확인해보기
- 사용자의 질문에 적합한 답변을 찾기 위해 청크화한 문서의 데이터를 검색하고, 내용을 그대로 추출함 
![2026-03-16 19 00 01.png](images%2F2026-03-16%2019%2000%2001.png)