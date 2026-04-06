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
