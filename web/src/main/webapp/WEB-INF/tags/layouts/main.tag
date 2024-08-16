<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>${title} | Polly</title>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/dist.css" />
    <link rel="icon" href="${pageContext.servletContext.contextPath}/img/poll-favicon.png" />
</head>

<body>
    <main class="container p-4 mx-auto">
        <article class="prose max-w-none">
            <h1>${title}</h1>

            <div>
                <c:forEach var="message" items="${toolbox.messages.all}">
                    <div class="message ${fn:toLowerCase(message.severity)}">
                        ${message.text}
                    </div>
                </c:forEach>
            </div>

            <jsp:doBody />
        </article>
    </main>
    <footer class="container mx-auto p-4 border-dotted border-t-4 border-[oklch(var(--a))]">
        Polly ${project.version} (revision <code>${git.commit.id.abbrev}</code>) is made with ❤️ + ☕ + <a href="https://dev.java/" target="_blank">Java ${systemInfo.javaVersion}</a> + <a href="https://jakarta.ee/" target="_blank">Jakarta EE 10</a>.
        Proudly running on ${systemInfo.javaRuntime}.
    </footer>
    <script defer src="${pageContext.servletContext.contextPath}/webjars/htmx.org/${htmx.version}/dist/htmx.min.js"></script>
</body>
</html>