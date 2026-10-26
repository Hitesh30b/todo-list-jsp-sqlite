package com.example.todo;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletContext;

public class TaskDAO {
    private final String url;

    public TaskDAO(ServletContext context) {
        String appPath = context.getRealPath("/");
        File dbFile = new File(appPath, "todo.db");
        this.url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        initialize();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "title TEXT NOT NULL, description TEXT, completed INTEGER NOT NULL DEFAULT 0)";
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection c = connect(); Statement s = c.createStatement()) {
                s.execute(sql);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize SQLite database.", e);
        }
    }

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, title, description, completed FROM tasks ORDER BY id DESC";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                tasks.add(new Task(rs.getInt("id"), rs.getString("title"),
                    rs.getString("description"), rs.getInt("completed") == 1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not load tasks.", e);
        }
        return tasks;
    }

    public void add(String title, String description) {
        String sql = "INSERT INTO tasks(title, description, completed) VALUES(?, ?, 0)";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, title.trim());
            p.setString(2, description == null ? "" : description.trim());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not add task.", e);
        }
    }

    public void update(int id, String title, String description, boolean completed) {
        String sql = "UPDATE tasks SET title=?, description=?, completed=? WHERE id=?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, title.trim());
            p.setString(2, description == null ? "" : description.trim());
            p.setInt(3, completed ? 1 : 0);
            p.setInt(4, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update task.", e);
        }
    }

    public void delete(int id) {
        try (Connection c = connect();
             PreparedStatement p = c.prepareStatement("DELETE FROM tasks WHERE id=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete task.", e);
        }
    }

    public Task findById(int id) {
        try (Connection c = connect();
             PreparedStatement p = c.prepareStatement(
                 "SELECT id, title, description, completed FROM tasks WHERE id=?")) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return new Task(rs.getInt("id"), rs.getString("title"),
                    rs.getString("description"), rs.getInt("completed") == 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find task.", e);
        }
        return null;
    }
}
