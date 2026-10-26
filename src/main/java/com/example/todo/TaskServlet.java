package com.example.todo;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {
    private TaskDAO dao;

    @Override
    public void init() {
        dao = new TaskDAO(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("editTask", dao.findById(id));
            } catch (NumberFormatException ignored) { }
        }
        request.setAttribute("tasks", dao.findAll());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                String title = request.getParameter("title");
                if (title != null && !title.trim().isEmpty()) {
                    dao.add(title, request.getParameter("description"));
                }
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String title = request.getParameter("title");
                if (title != null && !title.trim().isEmpty()) {
                    dao.update(id, title, request.getParameter("description"),
                        "on".equals(request.getParameter("completed")));
                }
            } else if ("toggle".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Task task = dao.findById(id);
                if (task != null) {
                    dao.update(id, task.getTitle(), task.getDescription(), !task.isCompleted());
                }
            } else if ("delete".equals(action)) {
                dao.delete(Integer.parseInt(request.getParameter("id")));
            }
        } catch (NumberFormatException ignored) {
            // Invalid IDs are ignored and the user is returned to the list.
        }
        response.sendRedirect(request.getContextPath() + "/tasks");
    }
}
