var antlr4Version = "4.13.2"

var grammarPackage = "org.pwr.grammar"

plugins {
    id("java")
    antlr
}

group = "org.pwr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = sourceCompatibility
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    antlr("org.antlr:antlr4:$antlr4Version")
}

tasks.generateGrammarSource {
    source = fileTree("src/main/antlr")
    outputDirectory = file("src/main/gen/$grammarPackage")
    arguments = listOf("-visitor", "-no-listener", "-package", grammarPackage)
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/gen", "src/main/java"))
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.generateGrammarSource)
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.generateGrammarSource)
}
