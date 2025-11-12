// Arquivo de build principal do projeto (nível raiz)

plugins {
    // A versão do AGP (Android Gradle Plugin) deve corresponder à sua
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

    // ✅ ADICIONADO: Define o plugin KSP (compatível com Kotlin 2.0.0)
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}