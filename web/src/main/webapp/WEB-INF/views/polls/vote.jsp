<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Cast your vote on #${poll.slug}">
  <p>Question: <strong>${poll.question}</strong></p>

  <form>
    <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
    <input type="hidden" name="ballot.ticketId" value="${ballot.ticketId} /">
    Your choices:
    <c:forEach items="${poll.options}" var="option">
      <input type="radio" name="vote.selectedOption" id="option-${option.getOptionValue()}" value="${option.getOptionValue()}">
      <label for="option-${option.getOptionValue()}">${option.getDisplayValue()}</label>
    </c:forEach>
    <input type="submit" value="Vote!" />
  </form>
  <p>
    <h2>Debugging info</h2>
    <dl>
      <dt>Your Ticket ID:</dt>
      <dd><pre>${ballot.ticketId}</pre></dd>

      <dt>Your Client Identifier:</dt>
      <dd><pre>${ballot.clientIdentifier}</pre></dd>
    </dl>
  </p>
</layout:main>