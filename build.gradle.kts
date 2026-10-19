import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    java
    application
    checkstyle
    jacoco
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

val sunChecks = configurations.create("sunChecks") {
    isTransitive = false
}

dependencies {
    implementation("org.springframework:spring-context:7.0.8")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
    add(sunChecks.name, "com.puppycrawl.tools:checkstyle:14.1.0")
}

application {
    mainClass = "vyshkat.Main"
}

val checkstyleConfig = layout.buildDirectory.file("checkstyle/sun_checks.xml")

val prepareCheckstyleConfig = tasks.register("prepareCheckstyleConfig") {
    inputs.files(sunChecks)
    outputs.file(checkstyleConfig)
    doLast {
        val standardConfig = resources.text
            .fromArchiveEntry(sunChecks, "sun_checks.xml")
            .asString()
        val configWithoutPackageAndFieldJavadocs = standardConfig
            .replace("<module name=\"JavadocPackage\"/>", "")
            .replace("<module name=\"JavadocVariable\"/>", "")
        checkstyleConfig.get().asFile.apply {
            parentFile.mkdirs()
            writeText(configWithoutPackageAndFieldJavadocs)
        }
    }
}

checkstyle {
    toolVersion = "14.1.0"
    config = resources.text.fromFile(checkstyleConfig.get().asFile)
    isShowViolations = true
    isIgnoreFailures = false
    maxWarnings = 0
}

tasks.withType<Checkstyle>().configureEach {
    dependsOn(prepareCheckstyleConfig)
    reports {
        html.required = true
        xml.required = false
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.15"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required = true
        xml.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
