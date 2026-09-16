plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "net.clydo.clytil"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }

    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    listOf(
        "org.projectlombok:lombok:1.18.48",
        "org.jetbrains:annotations:26.1.0"
    ).forEach {
        compileOnly(it)
        annotationProcessor(it)
    }
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

tasks.wrapper {
    gradleVersion = "9.7.1"
    distributionType = Wrapper.DistributionType.BIN
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
