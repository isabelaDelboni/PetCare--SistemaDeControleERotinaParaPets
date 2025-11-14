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

Após isso, o arquivo surge em: 

```bash
app/build/outputs/
```

---
## 👩‍💻 Isabela Escolaro Delboni — Arquitetura, Banco Local e Sincronização

Isa ficou responsável pela espinha dorsal técnica do app: estrutura, organização dos módulos e comunicação entre camadas. Suas tarefas envolvem toda a base do MVVM e a integração entre Room e Firebase.

### Responsabilidades Detalhadas

* Definir toda a estrutura de pacotes seguindo o padrão MVVM (Model–View–ViewModel).
* Configurar dependências principais do projeto:
    * Room para banco local,
    * Firebase,
    * Navigation Component,
    * Hilt para injeção de dependência.
* Criar todas as entidades essenciais:
    * `Pet`,
    * `Evento`,
    * `Usuario`.
* Implementar DAOs (`PetDao`, `EventoDao`).
* Criar repositórios utilizando Repository Pattern:
    * `PetRepository`,
    * `EventoRepository`.
* Implementar sincronização automática Room ↔ Firebase, utilizando:
    * WorkManager,
    * Workers de upload/download,
    * Estratégias de resolução de conflito (local vs remoto).
* Criar e configurar os ViewModels principais:
    * `PetViewModel`,
    * `EventoViewModel`,
    * `AuthViewModel`.
* Realizar testes completos de CRUD tanto local quanto remoto.
* Testar sincronização offline/online.
* Documentar:
    * Diagrama de classes,
    * Diagrama do banco,
    * Descrição técnica de arquitetura.
* Criar o README final com prints e orientações.
* Participar da apresentação final.

---
## 🎨 Mateus Henrique Escolaro — Telas, UI/UX e Navegação

O Mateus é responsável por todas as telas do aplicativo, seguindo o fluxo pensado no figma/protótipo do sistema e garantindo experiência e navegação fluida via Jetpack Compose.

### Responsabilidades Detalhadas

* Criar telas em Jetpack Compose, incluindo:
    * Login,
    * Meus Pets,
    * Cadastro de Pet.
* Criar telas adicionais:
    * Tela de Detalhes do Pet,
    * Tela de Cadastro de Evento,
    * Tela de Lembretes,
    * Tela de Configurações.
* Configurar a navegação usando Compose Navigation:
    * Rotas,
    * NavHost,
    * Navegação entre fluxos (auth → app).
* Criar componentes reutilizáveis:
    * Cards,
    * Inputs,
    * Botões,
    * Layouts de listas.
* Implementar feedback visual:
    * Toasts,
    * Loaders,
    * Mensagens de erro ou sucesso.
* Auxiliar nos testes de usabilidade.
* Garantir integração fluida com os ViewModels criados pela Isa.

---
## 🔥 David Rocha Neto — Firebase, Autenticação e Notificações

O David cuida de toda a parte de backend remoto usando Firebase, garantindo autenticação, armazenamento de dados e envio de notificações.

### Responsabilidades Detalhadas

* Configurar Firebase Authentication:
    * Login,
    * Registro,
    * Persistência do usuário logado.
* Configurar Firestore com a estrutura:
    ```
    /users/{uid}/pets/{petId}/events/{eventId}
    ```
* Configurar Firebase Storage para upload de fotos:
    * Pets,
    * Eventos.
* Implementar Firebase Cloud Messaging (FCM):
    * Notificações automáticas de eventos,
    * Agendamentos,
    * Tokens por usuário.
* Integrar Firebase com o WorkManager para sincronização:
    * Upload de dados offline,
    * Download de dados atualizados.
* Participar dos testes de CRUD local-remoto e sincronização.
* Ajudar na apresentação técnica.

---
## 📁 Observações Importantes (Acordadas pelo Grupo)

> * Toda implementação deve seguir a estrutura oficial das pastas: `data/local`, `data/remote`, `data/repository`, `ui`, `viewmodel` etc.
> * Alterações estruturais não devem ser feitas sem alinhamento.
> * Qualquer mudança precisa ser comunicada antes.
> * O projeto foi organizado previamente para evitar conflitos; manter esse padrão é essencial para a integração funcionar sem retrabalho.
Com certeza! Posso formatar seu texto para um arquivo `README.md` usando **Markdown** (linguagem de marcação), o que facilita a leitura e a cópia.

---

# 🗺️ Arquitetura e Modelagem de Dados

Este módulo documenta toda a **arquitetura lógica** do sistema, incluindo o **fluxo de navegação entre telas** e a **modelagem de dados** utilizada para representar pets, usuários, eventos e lembretes.

A estrutura foi desenhada para ser clara, escalável e completamente alinhada ao padrão adotado no projeto.

## 📱 1. Diagrama de Fluxo de Telas (Navigation Flow)

O fluxo abaixo representa a navegação real do aplicativo, mostrando como o usuário transita pelas telas principais.

### Telas e suas Funções

| Tela | Função |
| :--- | :--- |
| `LoginActivity` | Tela inicial de **autenticação**. |
| `HomeActivity` | **Hub central**; acesso a todos os módulos. |
| `ListaPetsActivity` | **Listagem de pets** do usuário. |
| `DetalhesPetActivity` | Informações completas do pet + eventos. |
| `EventoActivity` | Cadastro/edição de **eventos** do pet. |
| `LembreteActivity` | **Lembretes** vinculados aos eventos. |
| `PerfilUsuarioActivity` | Visualização do **perfil do usuário**. |
| `EditarPerfilActivity` | Edição dos **dados pessoais**. |
| `CadastroPetActivity` | Cadastro de **novos pets**. |
| `DetalhesRotaActivity` | Detalhes de **rotas** cadastradas. |
| `FavoritosActivity` | Pets ou itens **favoritados**. |
| `RotasActivity` | **Rotas** registradas pelo usuário. |

### 📥 Importar o Fluxo de Telas (Draw.io / Diagrams.net)

<img width="912" height="392" alt="image" src="https://github.com/user-attachments/assets/861c7cdb-2ebd-4f0f-9f4f-ea7e6a0df77a" />

<img width="1261" height="381" alt="Diagrama de Navegação entre Activitie 2 drawio" src="https://github.com/user-attachments/assets/05e54344-6888-41a4-80ae-e5aed3f02001" />


## 🗂️ 2. Diagrama Entidade-Relacionamento (ER)

Este diagrama representa a estrutura lógica do banco de dados, exibindo **entidades**, **atributos** e **relacionamentos** entre elas. É a base do modelo usado no **Firebase/Room**.

### Entidades e Relacionamentos

| Entidade | Chave Primária | Foreign Key | Relação |
| :--- | :--- | :--- | :--- |
| **Usuario** | `idUsuario` | --- | --- |
| **Pet** | `idPet` | `userId` → `Usuario(idUsuario)` | **Usuario 1:N Pet** |
| **Evento** | `idEvento` | `petId` → `Pet(idPet)` | **Pet 1:N Evento** |
| **Lembrete** | `idLembrete` | `eventoId` → `Evento(idEvento)` | **Evento 1:N Lembrete** |
| **Relatorio** | `idRelatorio` | `usuarioId` → `Usuario(idUsuario)` | **Usuario 1:1 Relatorio** |

### 🔗 Cardinalidades

* Um **usuário** pode ter **vários pets** (1:N).
* Um **pet** pode possuir **vários eventos** (1:N).
* Um **evento** pode gerar **vários lembretes** (1:N).
* Um **usuário** possui apenas **um relatório consolidado** (1:1).

### 📥 Importar ER (Draw.io / Diagrams.net)

<img width="677" height="440" alt="image" src="https://github.com/user-attachments/assets/b40c21a9-f9f8-4a75-8bc6-0da9e724470b" />

<img width="981" height="421" alt="Diagrama entidade 2 drawio" src="https://github.com/user-attachments/assets/2b5a845e-13f8-4bd4-adb4-685dac754018" />

