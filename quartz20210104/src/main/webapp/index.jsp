<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello </title>
</head>
<body>
    <h2 class="hello-title">Hello <%=request.getContextPath()%></h2>
    <br>
    <a href="http://127.0.0.1:8080<%=request.getContextPath()%>/get">Server Time</a>
    <form action="http://127.0.0.1:8080<%=request.getContextPath()%>/read" method="get">
  	 <input type="submit" value="Send"/>
    </form>
    
</body>
</html>