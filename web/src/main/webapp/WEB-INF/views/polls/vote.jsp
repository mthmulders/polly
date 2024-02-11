<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Cast your vote on #${poll.slug}">
  <p>Question: <strong>${poll.question}</strong></p>

  <c:if test="${not empty error}">
    <strong>Error: ${error.message}</strong>
  </c:if>

  <c:if test="${not empty ballot && ballot.usedAt == null}">
    <form method="post" action="">
      <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
      <input type="hidden" name="ballot.ticketId" value="${ballot.ticketId}" />
      Your choices:
      <c:forEach items="${poll.options}" var="option">
        <div class="pt-4">
          <input type="radio" name="vote.selectedOption" id="option-${option.optionValue}" value="${option.optionValue}" />
          <label for="option-${option.optionValue}">${option.displayValue}</label>
        </div>
      </c:forEach>
      <div class="pt-4">
        <input type="submit" class="btn btn-sm btn-primary" value="Vote!" />
      </div>
    </form>

    <!-- Debugging info: ticket_id=${ballot.ticketId}, client_identifier=${ballot.clientIdentifier} -->
  </c:if>
  <c:if test="${not empty ballot && ballot.usedAt != null}">
    You have already voted, thanks for your participation!
  </c:if>

</layout:main>