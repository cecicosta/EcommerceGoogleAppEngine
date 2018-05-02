<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="content" align="center">
<h2 align="center"> Busca de Usu�rio </h2>

<form:form action="/ecommerce/find_user_by_name" method="post" modelAttribute="user">
	<form:input type="text" path="name" name="search_user" placeholder="Nome do Usu�rio" class="search" required="required"/> <br />
	<input type="submit" value="Buscar" class="search-button">
</form:form>
</div>