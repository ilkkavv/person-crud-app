# Person CRUD app

## 📝 Description

**Person CRUD App** is a simple command-line application for managing a collection of persons. It supports basic CRUD
operations (create, read, update, delete) and is built using a clean layered architecture with a controller, repository
abstraction, and custom data structures. Data can be stored either in memory or in a CSV file.

## ✨ Current Features

- CLI-based CRUD operations for managing persons
- Supports both CSV and in-memory data storage
- Clean layered architecture with controller and repository abstraction
- Custom list implementation
- Automatic ID generation

## 🎓 How to Use

When the application starts, it opens an interactive command-line menu.  
You can choose an operation by entering either the full command or its short form shown in parentheses.

Available commands:

- `c` or `create` – create a new person
- `l` or `list` – list all persons
- `f` or `find` – find a person by ID
- `u` or `update` – update a person by ID
- `d` or `delete` – delete a person by ID
- `h` or `help` – show help information
- `e` or `exit` – exit the application

### Creating a person

Choose `create` and enter:

- first name
- last name
- age

The application automatically assigns a unique ID to the new person.

### Listing persons

Choose `list` to display all stored persons.

### Finding a person

Choose `find` and enter a person ID to view that person's details.

### Updating a person

Choose `update` and enter the ID of the person you want to modify.  
Then enter the new first name, last name, and age.

### Deleting a person

Choose `delete` and enter the ID of the person you want to remove.

### Repository option

By default, the application uses a CSV-based repository for persistent storage.  
You can also run the application with an in-memory repository:

```bash
java -jar person-crud-app.jar --repo=mem
```

> [!IMPORTANT]
> In-memory mode does not save data after the program exits.

## 📖 About

This project is developed as part of an **Object-Oriented Programming** course at **Tampere University of Applied
Sciences (TAMK)**.

The goal of the project is to apply core object-oriented programming principles in practice by designing and
implementing a small but structured application. The project focuses on concepts such as classes and objects,
encapsulation, abstraction, and the use of interfaces.

The application is built using a clean layered architecture, separating the user interface, controller, and data access
logic. It also includes custom data structures and interchangeable repository implementations to support maintainability
and extensibility.

Through this project, the aim is to develop skills in writing clear, maintainable, and well-structured code while
following common software development practices.

## 🏗️ Architecture

**Person CRUD app** follows a layered architecture with clear separation of concerns:

UI → Controller → Repository → Data Structure

The architecture allows switching between different UI and repository implementations without modifying other layers.

### Components

- **UI**
    - Handles user interaction
    - Reads input and displays output

- **Controller**
    - Acts as an intermediary between UI and repository
    - Contains application logic
    - Ensures separation between layers

- **Repository**
    - Responsible for data access and persistence
    - Two implementations:
        - CSV-based repository (persistent storage)
        - In-memory repository (for testing)

- **Data Structure**
    - Custom list implementation
    - Used to store and manage Person objects
    - The implementation can be easily replaced without affecting other parts of the application

### Design Principles

- Separation of concerns between layers
- Use of interfaces to allow interchangeable implementations
- Loose coupling between components
- Easy to extend with new UI or repository implementations

## 🧰 How to Compile and Run

### ❗ Requirements

- **Java 17** or newer (tested with **Java 25**)
- No separate **Gradle** installation required (**Gradle Wrapper** included)

### Compile:

```bash
./gradlew clean build
```

### Run:

```bash
java -jar build/libs/person-crud-app.jar
```

### Run with in-memory repository:
```bash
java -jar build/libs/person-crud-app.jar --repo=mem
```
