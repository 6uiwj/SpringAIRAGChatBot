plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}


group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {

	implementation(platform("org.springframework.ai:spring-ai-bom:1.1.0"))

	implementation("org.springframework.boot:spring-boot-starter-web")

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// coroutine
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	// Spring AI Gemini
	implementation("org.springframework.ai:spring-ai-starter-model-google-genai")

	// swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	// logging
	implementation("io.github.oshai:kotlin-logging:6.0.3")
}


kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
