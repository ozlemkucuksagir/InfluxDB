plugins {
	java
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
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
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation ("com.influxdb:influxdb-client-java:7.0.0")
	implementation ("com.influxdb:influxdb-client-flux:7.0.0")
	implementation("org.springframework:spring-webmvc:6.1.3")
	implementation ("javax.validation:validation-api:2.0.1.Final")
	implementation ("org.glassfish:jakarta.el:4.0.2")
	implementation ("org.apache.logging.log4j:log4j-api:2.23.0")
	implementation ("org.apache.logging.log4j:log4j-core:2.23.0")
	implementation ("org.springframework:spring-aspects:3.2.4.RELEASE")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

	compileOnly ("org.projectlombok:lombok:1.18.30")
	annotationProcessor ("org.projectlombok:lombok:1.18.30")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
