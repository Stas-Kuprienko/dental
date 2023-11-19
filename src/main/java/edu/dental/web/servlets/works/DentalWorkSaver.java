package edu.dental.web.servlets.works;

import edu.dental.database.DBService;
import edu.dental.database.DBServiceManager;
import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

public class DentalWorkSaver extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        WorkRecordBook recordBook = (WorkRecordBook) session.getAttribute("recordBook");

        String patient = request.getParameter("patient");
        String clinic = request.getParameter("clinic");
        String product = request.getParameter("product");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        LocalDate complete = LocalDate.parse(request.getParameter("complete"));

        try {
            I_DentalWork dw = recordBook.createRecord(patient, clinic, product, quantity, complete);
            DBService dbService = DBServiceManager.get().getDBService();
            dbService.getDentalWorkDAO(user).put(dw);
            System.out.println("OK!");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/welcome").forward(request, response);
        } catch (WorkRecordBookException | DatabaseException e) {
            System.out.println("Error!");
            request.getRequestDispatcher("/welcome").forward(request, response);
        }
    }
}