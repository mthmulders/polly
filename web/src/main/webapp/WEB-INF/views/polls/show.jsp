<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Now voting #${poll.slug}">
  <div class="flex flex-row flex-wrap">
    <div class="basis-full sm:basis-1/3 px-2">
      <h2>${poll.question} 👇🏻</h2>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="${qrCodeViewBox}" stroke="none" class="qr-code">
          ${qrCodeBody}
      </svg>
    </div>
    <div class="basis-full sm:basis-2/3 px-2"
         id="poll-summary"
         hx-get="${pageContext.servletContext.contextPath}/app/show/${poll.slug}/votes"
         hx-trigger="every 5s"
         hx-target="#poll-summary"
         hx-swap="innerHTML">
        <%@ include file="../fragments/polls/_votes.jsp" %>
    </div>
  </div>

</layout:main>