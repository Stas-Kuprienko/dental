package edu.dental.web.builders;

import edu.dental.domain.records.ProductMap;
import edu.dental.web.Repository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Iterator;

import static edu.dental.web.builders.HtmlTag.*;

public class ProductMapTable {

    private static final String href = "product-map";

    private final Iterator<ProductMap.Item> iterator;
    private final int id;

    public ProductMapTable(HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("user");
        ProductMap map = Repository.getInstance().getRecordBook(user).getMap();
        this.iterator = map.iterator();
        String strId = request.getParameter("id");
        id = (strId == null || strId.isEmpty()) ? 0 : Integer.parseInt(strId);
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        if (id > 0) {
            return next(id);
        }
        ProductMap.Item item = iterator.next();
        StringBuilder str = new StringBuilder();
        str.append(String.format(A_TR.o, href, item.getId()));
        DIV_TD.line(str, item.getKey());
        DIV_TD.line(str, String.valueOf(item.getValue()));
        str.append(A_TR.c);
        return str.toString();
    }

    private String next(int id) {
        ProductMap.Item item = iterator.next();
        StringBuilder str = new StringBuilder();
        if (item.getId() == id) {
            str.append(A_TR_WITHOUT_HREF.o);
            DIV_TD.line(str, item.getKey());
            str.append(String.format(FORM, item.getKey()));
        } else {
            str.append(String.format(A_TR.o, href, item.getId()));
            DIV_TD.line(str, item.getKey());
            DIV_TD.line(str, String.valueOf(item.getValue()));
        }
        str.append(A_TR.c);
        return str.toString();
    }


    private static final String FORM = """
                <div class="td" style="width: 40%%;">
                <form style="display: inline-block;" action="/dental/main/save-product">
                    <input style="width:96px;" type="number" name="price" value="">
                    <input type="submit" value="save">
                    <input type="hidden" name="title" value="%s">
                </form>&emsp;
                <form style="display: inline-block;" action="/dental/main/product-map">
                    <input type="submit" value="cancel">
                </form></div>""";
}