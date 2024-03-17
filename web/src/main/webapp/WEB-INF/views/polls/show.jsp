<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Now voting #${poll.slug}">
  <div class="flex flex-row">
    <div class="basis-1/4">
      <h2>${poll.question}</h2>
      <h3>Vote? 👉🏻</h3>
    </div>
    <div class="basis-1/4">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="${qrCodeViewBox}" stroke="none" class="qr-code">
          ${qrCodeBody}
      </svg>
    </div>
    <div class="basis-1/2">
        <h2>${voteCount} votes so far</h2>

        <div class="w-full">
            <c:forEach var="option" items="${votePercentages}">
            <div class="w-full"><strong>${option.key.displayValue}</strong> (${option.value * 100}%)</div>
            <div class="w-full inline">
                <div class="bg-accent" style="width: ${option.value * 100}%; height: 20px"></div>
                <div style="width: ${100 - (option.value * 100)}%; height: 20px"></div>
            </div>
            </c:forEach>
        </div>
    </div>
  </div>

</layout:main>