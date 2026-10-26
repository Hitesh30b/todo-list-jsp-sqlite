<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.example.todo.Task" %>
<%
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    Task editTask = (Task) request.getAttribute("editTask");
    String context = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My To-Do List</title>
  <link rel="stylesheet" href="<%= context %>/style.css">
</head>
<body>
  <main class="container">
    <header class="hero">
      <div class="eyebrow">PLAN • DO • DONE</div>
      <h1>My To-Do List</h1>
      <p>Keep your day organised, one task at a time.</p>
    </header>

    <section class="panel">
      <h2><%= editTask == null ? "Add a task" : "Update task" %></h2>
      <form method="post" action="<%= context %>/tasks" class="task-form">
        <input type="hidden" name="action" value="<%= editTask == null ? "add" : "update" %>">
        <% if (editTask != null) { %>
          <input type="hidden" name="id" value="<%= editTask.getId() %>">
        <% } %>
        <label for="title">Task title</label>
        <input id="title" name="title" maxlength="120" required
          placeholder="e.g. Finish assignment"
          value="<%= editTask == null ? "" : escapeHtml(editTask.getTitle()) %>">
        <label for="description">Description (optional)</label>
        <textarea id="description" name="description" rows="3"
          placeholder="Add a few details..."><%= editTask == null ? "" : escapeHtml(editTask.getDescription()) %></textarea>
        <% if (editTask != null) { %>
          <label class="check"><input type="checkbox" name="completed" <%= editTask.isCompleted() ? "checked" : "" %>> Mark as completed</label>
        <% } %>
        <div class="form-actions">
          <button class="primary" type="submit"><%= editTask == null ? "＋ Add task" : "Save changes" %></button>
          <% if (editTask != null) { %><a class="button secondary" href="<%= context %>/tasks">Cancel</a><% } %>
        </div>
      </form>
    </section>

    <section class="list-section">
      <div class="list-heading">
        <h2>Your tasks</h2>
        <span class="count"><%= tasks == null ? 0 : tasks.size() %> total</span>
      </div>
      <% if (tasks == null || tasks.isEmpty()) { %>
        <div class="empty"><div class="empty-icon">✓</div><h3>Nothing on your list yet</h3><p>Add your first task above to get started.</p></div>
      <% } else { %>
        <div class="task-list">
        <% for (Task task : tasks) { %>
          <article class="task-card <%= task.isCompleted() ? "completed" : "" %>">
            <form method="post" action="<%= context %>/tasks" class="toggle-form">
              <input type="hidden" name="action" value="toggle">
              <input type="hidden" name="id" value="<%= task.getId() %>">
              <button class="status" type="submit" title="Toggle completion"><%= task.isCompleted() ? "✓" : "" %></button>
            </form>
            <div class="task-content">
              <h3><%= escapeHtml(task.getTitle()) %></h3>
              <% if (task.getDescription() != null && !task.getDescription().trim().isEmpty()) { %>
                <p><%= escapeHtml(task.getDescription()) %></p>
              <% } %>
              <span class="badge"><%= task.isCompleted() ? "Completed" : "In progress" %></span>
            </div>
            <div class="task-actions">
              <a class="action edit" href="<%= context %>/tasks?action=edit&id=<%= task.getId() %>">Edit</a>
              <form method="post" action="<%= context %>/tasks" onsubmit="return confirm('Delete this task?');">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="<%= task.getId() %>">
                <button class="action delete" type="submit">Delete</button>
              </form>
            </div>
          </article>
        <% } %>
        </div>
      <% } %>
    </section>
    <footer>To-Do List • JSP + Java + SQLite</footer>
  </main>
</body>
</html>
<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;")
            .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }
%>
