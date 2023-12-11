package edu.dental.web.servlets.main;

import edu.dental.domain.Action;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/main/work-edit")
public class DentalWorkEditing extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        int id = Integer.parseInt(request.getParameter("id"));
        String field = request.getParameter("field");
        if (!(field == null || field.isEmpty())) {
            String value = request.getParameter("value");
            try {
                if (field.equals("product")) {
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    Action.addProductToWork(user, id, value, quantity);
                } else {
                    Action.editWork(user, id, field, value);
                }
            } catch (Action.ActionException e) {
                response.sendError(e.CODE);
                return;
            }
        }
        request.getRequestDispatcher("/main/work-handle").forward(request, response);
    }
}