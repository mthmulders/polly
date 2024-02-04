<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Thanks for your vote on #${poll.slug}">
    <p>Question: <strong>${poll.question}</strong></p>

    You voted

    <blockquote>${vote.option.displayValue}</blockquote>
</layout:main>