<%@ page import="edu.dental.domain.DatesTool" %>
<%@ page import="edu.dental.web.builders.HtmlTag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<nav class="menu">
    <header><strong>DENTAL MECHANIC SERVICE</strong></header>
    <a href="/dental/main/new-work">NEW WORK</a>
    <a href="dental/main/work-list">WORK LIST</a>
    <a href="/dental/main/new-product">PRODUCT MAP</a>
    <a href="/dental/main/product-map">REPORTS</a>
</nav>

    <% HtmlTag.Liner liner = new HtmlTag.Liner(request); %>
    <% HtmlTag.RowBuilder row = new HtmlTag.RowBuilder(request); %>

<body>
<section>
    <h3>
        <strong>
            <%=HtmlTag.month()%>
        </strong>
    </h3>
	<h4 style="float: left;">
        <label style="background-color: #002d73; border: 5px solid #002d73">CLOSED</label>&emsp;
        <label style="background-color: #075700; border: 5px solid #075700"> PAID </label>
    </h4>
	<div class="table">
	    <div class="thead">
            <div class="tr">
            <div class="th">PATIENT</div>
            <div class="th">CLINIC</div>
            <% while (liner.hasNext()) { %>
            <div class="th"><%=liner.next()%></div>
            <% } %>
            <div class="th">COMPLETE</div>
            <div class="th">ACCEPT</div>
        </div>
    </div>
    <div class="tbody">
        <% while (row.hasNext()) { %>
        <%=row.next()%>
        <% } %>
	</div></div>
</section>
</body>
</html>