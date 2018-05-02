<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="content" align="center">
<h2 align="center"> Login </h2>
<form:form action="/ecommerce/authenticate" method="post" modelAttribute="user">

<form:label path="login">Login: </form:label> <form:input type="text" path="login"/>
<form:label path="password">Senha: </form:label> <form:input type="password" path="password"/>

<input type="submit" value="Entrar" class="search-button">

</form:form>
</div>