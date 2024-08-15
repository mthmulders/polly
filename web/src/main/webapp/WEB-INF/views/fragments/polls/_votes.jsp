<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="poll-summary">
    <h2>Received ${voteCount} vote(s)</h2>

    <div class="w-full">
        <c:forEach var="option" items="${votePercentages}">
            <div class="w-full"><strong>${option.key.displayValue}</strong> (${option.value}%)</div>
            <div class="w-full inline">
                <div class="bg-accent" style="width: ${option.value}%; height: 20px"></div>
                <div style="width: ${100 - (option.value)}%; height: 20px"></div>
            </div>
        </c:forEach>
    </div>
</div>