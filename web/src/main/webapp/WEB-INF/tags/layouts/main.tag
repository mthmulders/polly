<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>${title} | Polly</title>
</head>

<body>
    <div>
        <main>
            <h1>${title}</h1>

            <div>
                <c:forEach var="message" items="${toolbox.messages.all}">
                    <div class="message ${fn:toLowerCase(message.severity)}">
                        ${message.text}
                    </div>
                </c:forEach>
            </div>

            <jsp:doBody />
        </main>
    </div>
</body>
</html>