# Person CRUD app

![Version](https://img.shields.io/badge/version-1.0.0--beta-orange)
![License](https://img.shields.io/badge/license-GPLv3-blue)
![Status](https://img.shields.io/badge/status-active%20development-green)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

## 📑 Table of Contents

- [Person CRUD app](#person-crud-app)
  - [📑 Table of Contents](#-table-of-contents)
  - [📝 Description](#-description)
  - [✨ Current Features](#-current-features)
  - [🎓 How to Use](#-how-to-use)
    - [Creating a person](#creating-a-person)
    - [Listing persons](#listing-persons)
    - [Finding a person](#finding-a-person)
    - [Searching for persons](#searching-for-persons)
    - [Sorting results](#sorting-results)
    - [Updating a person](#updating-a-person)
    - [Deleting a person](#deleting-a-person)
    - [Repository option](#repository-option)
  - [📖 About](#-about)
  - [🏗️ Architecture](#️-architecture)
    - [Components](#components)
    - [Design Principles](#design-principles)
    - [Data Handling](#data-handling)
    - [Encapsulation](#encapsulation)
    - [Error Handling and Results](#error-handling-and-results)
  - [🧰 How to Compile and Run](#-how-to-compile-and-run)
    - [❗ Requirements](#-requirements)
    - [Clone the repository](#clone-the-repository)
    - [Compile](#compile)
    - [Run](#run)
    - [Run with in-memory repository](#run-with-in-memory-repository)
  - [🧠 AI Usage](#-ai-usage)
  - [📄 License](#-license)

## Description

**Person CRUD App** is a Java application for managing a collection of persons through either a command-line interface
(CLI) or a graphical user interface (Swing GUI). The application supports full CRUD operations (create, read, update,
delete), searching, filtering, sorting, validation, and structured error handling.

The project is built using a clean layered architecture with separate UI, controller, and repository layers. Data can be
stored either persistently in a CSV file or temporarily in memory through interchangeable repository implementations.

The application also includes custom data structures, centralized validation and exception handling, and
**Log4j2**-based logging support. The architecture is designed to be modular, extensible, and easy to maintain while
demonstrating separation of concerns and object-oriented design principles.

---

![CLI Demo](assets/cli-demo.gif)

---

## Current Features

- Manage persons using either a Swing GUI or a command-line interface
- Create, view, update, and delete person records
- Search persons by ID or name
- Filter persons by age range
- Sort results by first name, last name, or age
- Supports both ascending and descending sorting
- Persistent CSV-based storage
- Optional in-memory repository mode
- Input validation with user-friendly error messages
- Validation highlighting and dialogs in the GUI
- Logging support and repository error handling

## Usage

By default, the application starts with the Swing-based graphical user interface.  
A command-line interface is also available through a startup option.

The application allows you to:

- Create new persons
- View all stored persons
- Find persons by ID or search by name
- Filter persons by age range
- Sort results by first name, last name, or age
- Update existing persons
- Delete persons
- View validation and error messages during input

Person records consist of:

- ID (generated automatically)
- First name
- Last name
- Age

> [!NOTE]
> Run the startup scripts from the root directory of the distribution package.
> This ensures that the `data` and `logs` directories are created in the correct location.

### User Interface Options

#### Swing GUI (default)

Start the graphical user interface:

**Linux / macOS**

```bash
./bin/person-crud-app
```

**Windows**

```powershell
bin\person-crud-app.bat
```

#### Command-Line Interface

Start the command-line interface:

**Linux / macOS**

```bash
./bin/person-crud-app --ui=cli
```

**Windows**

```powershell
bin\person-crud-app.bat --ui=cli
```

### Repository Options

By default, the application uses CSV-based persistent storage and stores data in the `data` directory.

You can also run the application using an in-memory repository:

**Linux / macOS**

```bash
./bin/person-crud-app --repo=mem
```

**Windows**

```powershell
bin\person-crud-app.bat --repo=mem
```

The options can also be combined:

```bash
./bin/person-crud-app --repo=mem --ui=cli
```

> [!IMPORTANT]
> In-memory mode does not save data after the program exits.

## About

This project is developed as part of an **Object-Oriented Programming** course at **Tampere University of Applied
Sciences (TAMK)**.

The goal of the project is to apply core object-oriented programming principles in practice by designing and
implementing a structured application. The project focuses on concepts such as classes and objects, encapsulation,
abstraction, interfaces, layered architecture, validation, and exception handling.

The application is built using a clean layered architecture that separates the user interface, controller, and data
access logic. It includes both Swing GUI and command-line interface implementations, interchangeable repository
implementations, custom data structures, validation layers, and centralized error handling to support maintainability
and extensibility.

Through this project, the aim is to develop skills in writing clear, maintainable, and well-structured code while
following common software development practices such as modular design, logging, documentation, and version control.

## Architecture

**Person CRUD app** follows a layered architecture with clear separation of concerns:

```text
App
 ↓
UI
 ↓
Controller
 ↓
Repository
 ↓
Data structure
```

The application is designed so that user interfaces and repository implementations can be changed without modifying the
core application logic.

### Main Layers

- Application entry point

  - Parses startup options
  - Selects the UI implementation
  - Selects the repository implementation
  - Starts the application
  
- UI layer

  - Contains both the Swing GUI and CLI implementations
  - Handles user interaction and displays results
  - Delegates application logic to the controller
  - Uses shared UI abstraction through `AppUi`
  
- Controller layer

  - Coordinates application operations
  - Validates input before repository access
  - Returns structured result objects to the UI
  - Keeps UI code separated from repository logic
  
- Repository layer

  - Provides data access through the `PersonRepository` interface
  - Includes CSV-based persistent storage
  - Includes in-memory storage for temporary use
  - Hides persistence details from the controller
  
- Model layer

  - Contains the person-related data objects
  - Separates input data from stored entities
  
- Validation layer

  - Provides structured validation errors
  - Identifies invalid fields using typed validation objects
  - Allows both CLI and GUI to display validation errors consistently
  
- Data structure layer

  - Contains custom list implementations
  - Provides a shared `MyList` abstraction
  - Includes array-based and linked-list-based implementations
  - `MyArrayList` is currently used as the default implementation because  
    the application frequently relies on index-based access and sorting operations

### Design Principles

- Separation of concerns between layers
- Interface-based design for interchangeable implementations
- Loose coupling between UI, controller, and repository
- Encapsulation of internal repository data
- Structured validation instead of plain error strings
- Centralized exception handling for technical failures

### Error Handling and Logging

The application separates expected application-level failures from technical failures.

Expected failures, such as validation errors or missing persons, are returned from the controller as structured result
objects. This allows both the CLI and GUI to display errors consistently without depending on repository details.

Technical failures, such as CSV read/write errors or invalid CSV content, are handled with custom repository exceptions.
These errors are logged using **Log4j2** and shown to the user through CLI error messages or GUI dialogs.

## 🧰 How to Compile and Run

### ❗ Requirements

- **Java 25** (tested)
- No separate **Gradle** installation required (**Gradle Wrapper** included)

### Clone the repository

```bash
git clone https://github.com/ilkkavv/person-crud-app
cd person-crud-app
```

### Compile

```bash
./gradlew clean build
```

### Run

```bash
java -jar build/libs/person-crud-app-1.0.0-beta.jar
```

The application runs as an interactive CLI and will prompt for user input.

### Run with in-memory repository

```bash
java -jar build/libs/person-crud-app-1.0.0-beta.jar --repo=mem
```

---

> [!NOTE]
> Alternatively, download the pre-built JAR from the GitHub Releases page.

## 🧠 AI Usage

**ChatGPT** (OpenAI GPT-5.3) was used during this project primarily as a learning aid. The tool was utilized to clarify
course concepts, assist with understanding error messages, and improve the quality of documentation (README, code
comments, and commit messages).

AI was not used to generate code, but rather to support learning and enhance documentation quality.

## 📄 License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**.

You are free to use, modify, and distribute this project, provided that any
derivative work is also distributed under the same license.

See the [LICENSE](LICENSE) file for details.
