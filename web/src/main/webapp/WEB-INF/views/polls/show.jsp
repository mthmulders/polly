<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="${pollInstance.poll().slug()} @ ${pollInstance.slug()}">
  <p>Question: <strong>${pollInstance.poll().question()}</strong></p>
  <p>Vote? Go to <a href="${voteUrl}">${voteUrl}</a></p>
</layout:main>