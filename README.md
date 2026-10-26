# To-Do List (JSP + SQLite)

A CRUD web application built with Java Servlets, JSP, SQLite, and Maven for use in NetBeans.

## Requirements
- JDK 11 or later
- Apache NetBeans with Maven support
- Apache Tomcat 10.1 (Jakarta Servlet 6.0)
- Internet access on first Maven build to download dependencies

## Open and run in NetBeans
1. Extract the ZIP file.
2. In NetBeans choose **File → Open Project** and select the `todo_list_jsp_sqlite` folder (the folder containing `pom.xml`).
3. Configure Tomcat 10.1 as a server in NetBeans.
4. Right-click the project and choose **Clean and Build**.
5. Choose **Run**. Open the URL shown by NetBeans; the app routes to `/tasks`.

## Features
- Create tasks with a title and optional description
- View all saved tasks
- Edit task details and completion status
- Mark/unmark tasks as completed
- Delete tasks after confirmation

## Database
SQLite is initialized automatically when the application starts. The `todo.db` file is created under the deployed web application's root directory. `database/schema.sql` documents the schema.

## Notes
- This starter project targets Tomcat 10.1 / Jakarta Servlet 6.0. If your lab uses Tomcat 9, the code must be converted from `jakarta.*` to `javax.*` and the servlet dependency changed accordingly.
- For a persistent production database, configure a writable data directory outside the deployed application directory.
