package edu.dental.servlets;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.entities.DentalWork;
import edu.dental.service.Repository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@WebServlet("/main/reports/download")
public class ReportDownloader extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream output = response.getOutputStream();
        try {
            int userId = (int) request.getAttribute(WebAPI.INSTANCE.paramUser);
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            ReportService reportService = ReportService.getInstance();
            List<DentalWork> works;
            WorkRecordBook recordBook = Repository.getInstance().getRecordBook(userId);
            if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
                works = recordBook.getRecords();
            } else {
                works = reportService.getReportFromDB(userId, Integer.parseInt(month), Integer.parseInt(year));
            }
            response.setContentType("application/msword");
            String fileFormat = ReportService.getInstance().getFileFormat();
            String fileName = getFileName(year, month);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + fileFormat + "\"");
            reportService.writeReportToOutput(output, recordBook.getProductMap().keysToArray(), works);
        } catch (ReportServiceException e) {
            response.sendError(500);
        }
    }


    private String getFileName(String year, String month) {
        if (year == null || year.isEmpty() && month == null || month.isEmpty()) {
            LocalDate now = LocalDate.now();
            return now.getMonth() + "_" + now.getYear();
        } else {
            return Month.of(Integer.parseInt(month)) + "_" + year;
        }
    }
}
