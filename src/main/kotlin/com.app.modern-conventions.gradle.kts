plugins {
    java
    id("com.diffplug.spotless")
}

// All --add-opens flags required for Lombok, MapStruct, and javac tools on Java 21+
private val javaAddOpens = listOf(
    "--add-opens", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
    "--add-opens", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
)

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.isFork = true
    options.forkOptions.jvmArgs = (options.forkOptions.jvmArgs ?: mutableListOf()).apply {
        addAll(javaAddOpens)
    }
    options.compilerArgs.addAll(listOf(
        "-Xlint:all",
        "-Xlint:-serial",
        "-Xlint:-processing",
        "-Xdoclint:none"
    ))
}

spotless {
    java {
        // 1.27.0+ is required for Java 25 binary compatibility (getDiagnostics() API change)
        googleJavaFormat("1.27.0")
    }
}

tasks.named("check") {
    dependsOn("spotlessCheck")
}
// Note: repositories are managed centrally via dependencyResolutionManagement in settings.gradle.kts
// Do NOT add a repositories {} block here — it would override the settings-level declarations.
