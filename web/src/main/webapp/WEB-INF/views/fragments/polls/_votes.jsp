<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="poll-summary">
    <h2>Received ${voteCount} vote(s)</h2>

    <div class="w-full">
        <c:forEach var="item" items="${votePercentages}">
            <div class="w-full"><strong>${item.optionLabel()}</strong> (${item.percentage()}%)</div>
            <div class="w-full inline">
                <div class="bg-accent" style="width: ${item.percentage()}%; height: 20px"></div>
                <div style="width: ${100 - (item.percentage())}%; height: 20px"></div>
            </div>
        </c:forEach>
    </div>
</div>