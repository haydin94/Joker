<%-- 
    Document   : index
    Created on : 22.08.2017, 16:50:23
    Author     : aydins
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><b>JOKEE!</b></h1>
        <form action="${pageContext.request.contextPath}/Servlet" method="get">
            <input type="submit" name="button1" value="Go to Request Site" />
        </form>
    </body>
</html>
