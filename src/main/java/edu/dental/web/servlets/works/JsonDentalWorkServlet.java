package edu.dental.web.servlets.works;

import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.web.JsonObjectParser;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/dental-works")
public class JsonDentalWorkServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        List<DentalWorkDTO> works = Repository.getInstance().getDentalWorkDtoList(user);
        String json = JsonObjectParser.getInstance().parseToJson(works.toArray());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
