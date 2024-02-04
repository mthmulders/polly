<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Thanks for your vote on #${poll.slug}">
    <div class="card w-96 bg-neutral shadow-xl">
        <div class="card-body">
            <h2 class="card-title">${poll.question}</h2>
            <p>${vote.option.displayValue}</p>
        </div>
    </div>
</layout:main>