<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="content" align="center">
<h2 align="center"> Busca de Produtos </h2>
<form:form action="/ecommerce/find_product_by_name" method="post" modelAttribute="product">
	<form:input type="text" path="name" name="search_product" placeholder="Nome do Produto" class="search" required="required"/> <br />
	<input type="submit" value="Buscar" class="search-button">
</form:form>
</div>