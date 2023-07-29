<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Cast your vote on #${poll.slug}">
  <p>Question: <strong>${poll.question}</strong></p>

  <form>
    <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
    <input type="hidden" name="ballot.ticketId" value="${ballot.ticketId()} /">
    Your choices:
    <ul>
      <c:forEach items="${poll.options}" var="option">
        <li>${option.displayValue()}</li>
      </c:forEach>
    </ul>
  </form>
</layout:main>