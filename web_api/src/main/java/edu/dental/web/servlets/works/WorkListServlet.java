package edu.dental.web.servlets.works;

import edu.dental.dto.DentalWork;
import edu.dental.dto.ProductMap;
import edu.dental.web.JsonObjectParser;
import edu.dental.web.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/main/work-list")
public class WorkListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        String url = "http://localhost:8081/dental/api/dental-works?user=" + user;
        String json = getJson(url, request.getSession().getId());
        DentalWork[] works = (DentalWork[]) JsonObjectParser.getInstance().parseFromJson(json, DentalWork[].class);
        request.setAttribute("works", works);

        //temporary
        ProductMap mapDTO = new ProductMap(Repository.getInstance().getRecordBook(user).getMap());
        request.setAttribute("map", mapDTO);
        // *** //

        request.getRequestDispatcher("/main/work-list/page").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private String getJson(String resource, String sessionId) throws IOException {
        URL url = new URL(resource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Cookie", "JSESSIONID=" + URLEncoder.encode(sessionId, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder str = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            str.append(line);
        }
        return str.toString();
    }
}
