plugins {
    id("java")
    checkstyle
    id("com.github.spotbugs") version "6.5.4"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
    application
}



group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("ui.Main")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

spotbugs {
    effort.set(com.github.spotbugs.snom.Effort.MAX)
    reportLevel.set(com.github.spotbugs.snom.Confidence.MEDIUM)
    ignoreFailures.set(true)
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports.create("html") {
        required.set(true)
    }
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
        html.stylesheet = resources.text.fromFile("config/checkstyle/checkstyle-noframes-severity-sorted.xsl")
    }
}

checkstyle{
    isIgnoreFailures = true
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
}

tasks.build {
    dependsOn("pitest")
}

pitest {
    targetClasses.set(setOf("board.*", "game.*", "piece.*", "player.*"))
    targetTests.set(setOf("board.*", "game.*", "piece.*", "player.*"))

    junit5PluginVersion.set("1.2.1")
    pitestVersion.set("1.15.0")

    threads.set(4)
    outputFormats.set(setOf("HTML"))
    timestampedReports.set(false)
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true)
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage.set(true)
}