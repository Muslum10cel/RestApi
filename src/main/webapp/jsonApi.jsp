<%-- 
    Document   : jsonApi
    Created on : Mar 31, 2016, 5:43:18 PM
    Author     : muslumoncel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>


        <button onclick="myFunction()">Call ws</button>
        <h1>Hello World!</h1>
        <script>
            function myFunction() {
                $.ajax({
                    url: "",
                    data: "", //ur data to be sent to server
                    contentType: "application/json; charset=utf-8",
                    type: "GET",
                    success: function (data) {
                        alert(data);
                    },
                    error: function (x, y, z) {
                        alert(x.responseText + "  " + x.status);
                    }
                });
            }
        </script>
    </body>
</html>
