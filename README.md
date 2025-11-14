# 🐾 PetCare – Sistema de Controle e Rotina para Pets

Aplicativo Android para organização e monitoramento da rotina de animais de estimação.

## ✨ Visão Geral

O **PetCare** é um app Android desenvolvido para ajudar tutores a manterem uma rotina saudável e organizada para seus pets. Ele funciona como um hub onde o usuário pode registrar informações importantes, acompanhar atividades diárias e receber lembretes essenciais para garantir o bem-estar do animal.

O projeto foi construído com foco em:

* Arquitetura limpa e escalável
* Organização modular
* Manutenibilidade
* Uso de tecnologias modernas do ecossistema Android
* Integração com Firebase para persistência e autenticação

O sistema já está estruturado de forma completa para evoluções futuras, como novas features, novos módulos e sincronização entre dispositivos.

---

## 🏗 Arquitetura e Tecnologias

O projeto combina três pilares:

1.  **MVVM (Model–View–ViewModel)**
    * Separação clara de responsabilidades, permitindo que a View (telas) observe mudanças de estado vindas do ViewModel.
2.  **Clean Architecture**
    * Camadas bem definidas, baixo acoplamento e testabilidade garantida.
3.  **Boas práticas Android**
    * Componentes modernos, padronização visual e uso de recursos eficientes.

### Tecnologias utilizadas

* **Kotlin**
* **Android Jetpack**
    * LiveData / StateFlow (dependendo da implementação)
    * ViewModel
    * Navigation
    * ViewBinding
* **Firebase**
    * Authentication
    * Firestore / Realtime Database
    * Storage (se houver uploads de imagem)
* **Dependency Injection**
    * Hilt ou Koin (presente na pasta `di/`)
* **Coroutines**
    * Para operações assíncronas
* **Repository Pattern**
* **Gradle Kotlin DSL**

---

## 📂 Estrutura do Projeto

Estrutura da pasta `app/src/main/java`:

```

com.example.petcaresistemadecontroleerotinaparapets
│
├── data/
│   ├── model/         \# Modelos / entidades
│   ├── repository/    \# Repositórios (Firebase / local)
│   └── datasource/    \# Fontes de dados (remoto/local, DTOs)
│
├── di/
│   └── modules/       \# Módulos de injeção de dependência
│
├── presentation/
│   ├── home/          \# Telas e fragments
│   ├── pets/          \# Listagem, detalhes, cadastro
│   ├── login/         \# Autenticação
│   ├── vaccines/      \# Controle de vacinas
│   ├── reminders/     \# Lembretes e agenda
│   └── adapters/      \# Adapters de RecyclerView
│
├── viewmodel/
│   ├── PetViewModel.kt
│   ├── AuthViewModel.kt
│   ├── VaccineViewModel.kt
│   └── ReminderViewModel.kt
│
├── ui/
│   ├── components/    \# Views customizadas
│   └── themes/        \# Estilos e cores do app
│
├── utils/
│   ├── extensions/    \# Extensões úteis
│   └── formatting/    \# Formatadores de datas e afins
│
├── PetCareApplication.kt
└── MainActivity.kt

````

Essa estrutura deixa evidente:

* onde ficam as telas,
* onde fica a lógica,
* onde os dados são acessados,
* onde a arquitetura é configurada.

Isso ajuda muito no crescimento sustentável do projeto.

---

## 🎯 Funcionalidades Detalhadas

* **Cadastro de Pets**
    * Nome, foto, idade, espécie, peso, raça.
    * Armazenamento seguro no Firebase.
    * Suporte para múltiplos pets por usuário.
* **Rotina e Atividades**
    * Alimentação
    * Banho
    * Higiene
    * Passeios
    * Medicação
    * Cada atividade pode ser registrada com:
        * horário
        * descrição
        * periodicidade (diária/semanal)
* **Controle de Vacinas**
    * Registro do nome da vacina
    * Data de aplicação
    * Data de reforço
    * Notificações e lembretes
* **Lembretes Inteligentes**
    * Notificações automáticas
    * Organização por pet
    * Marcação de concluída/não concluída
* **Autenticação e Perfil**
    * Login com e-mail e senha
    * Persistência de sessão
    * Atualização de dados do tutor

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

* Android Studio Flamingo (ou superior)
* JDK 17+
* Gradle configurado (já incluso)
* Conta no Firebase
* Dispositivo físico ou emulador API 24+

### Passo a Passo

1.  Clone o repositório:
    ```bash
    git clone [https://github.com/seu-usuario/PetCare.git](https://github.com/seu-usuario/PetCare.git)
    ```
2.  Abra o projeto no Android Studio.
3.  Aguarde o Gradle sincronizar.
4.  Baixe o arquivo `google-services.json` no Firebase.
5.  Coloque o arquivo em: `app/google-services.json`
6.  Rode o app no emulador ou celular.

---

## 🌐 Endpoints / Estrutura de Dados

Se estiver usando **Firebase Firestore**, um exemplo de estrutura pode ser:

````

📁 Collection: users
users/{userId}
\- name: string
\- email: string
\- createdAt: timestamp

📁 Collection: pets
users/{userId}/pets/{petId}
\- name: string
\- breed: string
\- photoUrl: string
\- age: number
\- createdAt: timestamp

📁 Collection: vaccines
users/{userId}/pets/{petId}/vaccines/{vaccineId}
\- name: string
\- appliedAt: timestamp
\- boosterAt: timestamp

📁 Collection: reminders
users/{userId}/reminders/{reminderId}
\- title: string
\- date: timestamp
\- petId: string
\- completed: boolean

````

> Caso use uma API própria, você pode substituir esta seção por endpoints REST.

---

## 🧪 Testes

### Testes Unitários

Localizados em: `app/src/test/`

Para rodar:

```bash
./gradlew test
````

### Testes Instrumentados

Localizados em: `app/src/androidTest/`

Rodar pelo Android Studio → Run Tests

-----

## 📦 Build e Deploy

Criar APK:

```bash
./gradlew assembleDebug
```

Criar App Bundle:

```bash
./gradlew bundleRelease
```

Após isso, o arquivo surge em: `app/build/outputs/`

```
```
