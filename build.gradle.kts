// Arquivo de build principal do projeto (nível raiz)

plugins {
    id("com.android.application") version "8.5.2" apply false // (Sua versão pode ser 8.13.1)
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false // (Sua versão pode ser 2.51.1)
    id("com.google.gms.google-services") version "4.4.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
