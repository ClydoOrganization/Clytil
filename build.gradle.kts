plugins {
    java
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "9.7.0"
}

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

lombok {
    version = "1.18.48"
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.1.0")
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
