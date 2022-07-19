<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello </title>
</head>
<body>
    <h2 class="hello-title">Hello Scheduler</h2>

<c:set var="cpath" value="<%=request.getContextPath()%>" scope="application" />
<f:form modelAttribute="scheduleVo" method="post" action="${cpath}/schedule/post">

<table border="1">
        <tr>
            <th>Schedule in cron expression(0 12 * * * ? *)</th>
            <td>
            <f:input path="datetimeCronExpression"/>
                </td>
        </tr>
        <tr>
            <th>Schedule name</th>
            <td><f:input path="memo"/>
                </td>
        </tr>
</table>
<br/>
<div>
    <input type="submit" value="Post">
</div>
</f:form>
    
</body>
</html>
