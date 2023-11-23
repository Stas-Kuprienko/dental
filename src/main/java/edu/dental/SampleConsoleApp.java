package edu.dental;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.database.mysql_api.dao.DentalWorkMySql;
import edu.dental.database.mysql_api.dao.ProductMapMySql;
import edu.dental.database.mysql_api.dao.UserMySql;
import edu.dental.domain.APIManager;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.domain.records.my_work_record_book.MyProductMap;
import edu.dental.domain.reports.MonthlyReport;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.utils.DatesTool;
import edu.dental.utils.data_structures.SimpleList;

import java.time.LocalDate;
import java.util.Scanner;

public class SampleConsoleApp {

    public static Scanner in = new Scanner(System.in);
    public static User user;
    public static WorkRecordBook workRecordBook;
    public static ReportService reportService;

    public static DBService dbService;

    public static void main(String[] args) {
        while (true) {
            try {
                menu_1();
            } catch (AuthenticationException | WorkRecordBookException | DatabaseException ignore) {
            }
            while (user != null) {
                try {
                    menu_2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }




    static class Enter {
        public static void newAccount() throws DatabaseException {
            String name;
            String login;
            String password;
            System.out.print("name: ");
            name = in.next();
            System.out.print("login: ");
            login = in.next();
            System.out.print("password: ");
            password = in.next();
            User newUser = new User(name, login, password);
            System.out.println(new UserMySql().put(newUser));
            user = newUser;
            workRecordBook = APIManager.instance().getWorkRecordBook();
            reportService = APIManager.instance().getReportService(user);
            System.out.println("Welcome!");
        }

        public static void logIn() throws AuthenticationException, DatabaseException {
            String login = "stas.com";
            String password = "1234";
//            System.out.print("login: ");
//            login = in.next();
//            System.out.print("password: ");
//            password = in.next();
            user = Authenticator.authenticate(login, password);
            SimpleList<I_DentalWork> records = (SimpleList<I_DentalWork>) new DentalWorkMySql(user).getAll();
            MyProductMap productMap = (MyProductMap) APIManager.instance().getProductMap(user);
            workRecordBook = APIManager.instance().getWorkRecordBook(records, productMap);
            reportService = APIManager.instance().getReportService(user);
            System.out.println("Welcome!");
        }

        public static void logOut() {
            user = null;
            workRecordBook = null;
        }
    }



    static class ProductMapping{
        public static void add() throws DatabaseException {
            String product;
            System.out.println("product: ");
            product = in.next();
            System.out.println("price: ");
            int price = in.nextInt();
            int id;
            System.out.println(id = new ProductMapMySql(user).put(product, price));
            workRecordBook.getMap().put(product, price, id);

        }

        public static void edit() throws DatabaseException {
            String product;
            System.out.println("the product you want to edit: ");
            product = in.next();
            System.out.println("set new price: ");
            int price = in.nextInt();
            @SuppressWarnings("all")
            int id = workRecordBook.getMap().put(product, price);
            System.out.println(new ProductMapMySql(user).edit(id, price));
        }

        public static void delete() throws DatabaseException {
            String product;
            System.out.println("the product you want to delete: ");
            product = in.next();
            int id = workRecordBook.getMap().remove(product);
            System.out.println(new ProductMapMySql(user).delete(id));
        }

        public static void open() throws DatabaseException {
            System.out.println(workRecordBook.getMap().toString());
            System.out.println("""
                    1 - add new;
                    2 - edit;
                    3 - delete;
                    """);
            int action = in.nextInt();
            switch (action) {
                case 1 -> add();
                case 2 -> edit();
                case 3 -> delete();
            }
        }
    }



    static class EntryRecord {
        public static void makeNew() throws WorkRecordBookException, DatabaseException {
            System.out.print("patient: ");
            String patient;
            patient = in.next();
            System.out.print("clinic: ");
            String clinic;
            clinic = in.next();
            System.out.print("product: ");
            in.nextLine();
            String product;
            product = in.nextLine();
            System.out.print("quantity: ");
            int quantity = in.nextInt();
            System.out.print("complete: ");
            String complete;
            complete = in.next();
            DentalWork workRecord;
            if (product.isEmpty()) {
                workRecord = (DentalWork) workRecordBook.createRecord(patient, clinic);
            } else {
                workRecord = (DentalWork) workRecordBook.createRecord(patient, clinic, product, quantity, complete);
            }
            System.out.println(new DentalWorkMySql(user).put(workRecord));
        }

        public static void edit() throws WorkRecordBookException, DatabaseException {
            SimpleList<I_DentalWork> workRecords = (SimpleList<I_DentalWork>) workRecordBook.getList();
            for (I_DentalWork wr : workRecords) {
                System.out.println(wr);
            }
            int id = in.nextInt();
            DentalWork workRecord = (DentalWork) getByID(workRecords, id);
            if (workRecord == null) {
                return;
            }
            System.out.println(edits);
            int action = in.nextInt();
            switch (action) {
                case 1 -> {
                    String patient = in.next();
                    workRecord.setPatient(patient);
                }
                case 2 -> {
                    String clinic = in.next();
                    workRecord.setClinic(clinic);
                }
                case 3 -> {
                    System.out.println("product: ");
                    String product = in.next();
                    System.out.println("quantity: ");
                    int q = in.nextInt();
                    workRecordBook.addProductToRecord(workRecord, product, q);
                }
                case 4 -> {
                    String completeStr = in.next();
                    LocalDate complete = DatesTool.toLocalDate(completeStr);
                    workRecord.setComplete(complete);
                }
                case 5 -> {
                    String statusStr = in.next();
                    I_DentalWork.Status status = Enum.valueOf(I_DentalWork.Status.class, statusStr);
                    workRecord.setStatus(status);
                }
                case 6 -> {
                    String comment = in.next();
                    workRecord.setComment(comment);
                }
                case 0 -> {
                    return;
                }
            }
            System.out.println(new DentalWorkMySql(user).edit(workRecord));
        }
        private static I_DentalWork getByID(SimpleList<I_DentalWork> workRecords, int id) {
            for (I_DentalWork wr : workRecords) {
                if (wr.getId() == id) {
                    return wr;
                }
            } return null;
        }
        static String edits = """
                1 - patient;
                2 - clinic;
                3 -  add product;
                4 - complete;
                5 - status;
                6 - add comment;
                """;
    }



    public static class Reports {

        static SimpleList<I_DentalWork> closed;

        public static void sorting() throws DatabaseException {
            closed = (SimpleList<I_DentalWork>) workRecordBook.sorting();
            DentalWorkMySql dao = new DentalWorkMySql(user);
            System.out.println(dao.setFieldValue(closed, "status", I_DentalWork.Status.CLOSED));

            int reportId;
            System.out.println(reportId = dao.setReportId(closed));
            for (I_DentalWork dw : closed) {
                dw.setReportId(reportId);
            }
            System.out.println(closed);
        }

        public static void saveFile() throws ReportServiceException {
            System.out.println("year:");
            int year = in.nextInt();
            System.out.println("month:");
            String month = in.next();
            LocalDate today = LocalDate.now();
            if (year == today.getYear() && month.equalsIgnoreCase(today.getMonth().toString())) {
                System.out.println(reportService.saveReportToFile(workRecordBook.getMap(), new MonthlyReport(workRecordBook.getList())));
                return;
            }
            MonthlyReport report = reportService.getReportFromDB(month, String.valueOf(year));
            System.out.println(reportService.saveReportToFile(workRecordBook.getMap(), report));
        }

        public static void getReportByMonth() throws ReportServiceException {
            System.out.println("year:");
            int year = in.nextInt();
            System.out.println("month:");
            String month = in.next();
            MonthlyReport report = reportService.getReportFromDB(month, String.valueOf(year));
            System.out.println(report);
            System.out.println("""
                    1 - save to file;
                    2 - count salary;
                    3 - select another month;
                    """);
            int r = in.nextInt();
            switch (r) {
                case 1 -> System.out.println(reportService.saveReportToFile(workRecordBook.getMap(), report));
                case 2 -> System.out.println(count((SimpleList<I_DentalWork>) report.getDentalWorks()));
                case 3 -> getReportByMonth();
            }
        }

        public static void salaryStatistic() throws ReportServiceException {
            System.out.println(reportService.saveSalariesToFile());
        }

        private static int count(SimpleList<I_DentalWork> list) {
            int n = 0;
            for (I_DentalWork dw : list) {
                if (!dw.getProducts().isEmpty()) {
                    for (Product p : dw.getProducts()) {
                        n += p.countAmount();
                    }
                }
            }
            return n;
        }
    }



    static String options_1 = """
            1 - log in;
            2 - create new account;
            """;

    static String options_2 = """
            1 - open product map;
            2 - add new record;
            3 - edit a record;
            4 - open records;
            5 - sorting;
            6 - save a report;
            7 - get monthly report;
            8 - count all salaries;
            """;


    public static void menu_1() throws AuthenticationException, WorkRecordBookException, DatabaseException {
        System.out.println("welcome to DentApp!\n" + options_1);
        int i = in.nextInt();
        switch (i) {
            case 1 -> Enter.logIn();
            case 2 -> Enter.newAccount();
            case 0 -> System.exit(0);
            default -> Enter.logOut();
        }
    }

    public static void menu_2() throws WorkRecordBookException, DatabaseException, ReportServiceException {
        dbService = APIManager.instance().getDBService();
        System.out.println(user.getName() + ", you are enter in service\n" + options_2);
        int j = in.nextInt();
        switch (j) {
            case 1 -> ProductMapping.open();
            case 2 -> EntryRecord.makeNew();
            case 3 -> EntryRecord.edit();
            case 4 -> {
                for (I_DentalWork dw : workRecordBook.getList()) System.out.println(dw);
                EntryRecord.edit();
            }
            case 5 -> Reports.sorting();
            case 6 -> Reports.saveFile();
            case 7 -> Reports.getReportByMonth();
            case 8 -> Reports.salaryStatistic();
            case 0 -> Enter.logOut();
            default -> System.out.println("wrong input");
        }
    }
}
