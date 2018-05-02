<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="content">
<h2 align="center"> Listagem de Usu�rios </h2>
<table class="table-content">
    
    <tr>
        <td> <b>Login</b> </td>
        <td> <b>Nome</b> </td>
        <td> <b>E-mail</b> </td>
        <td> <b>Telefone</b> </td>
        <td> <b>Nascimento</b> </td>
        <td> <b>Foto</b> </td>
        
 	</tr>
 	
<c:forEach var="u" items="${userList}">
	<!-- Para cada produto, repetir o código entre o <tr> </tr> abaixo -->
    <tr>
        <td> ${u.login} </td>
        <td> ${u.name} </td>
        <td> ${u.email} </td>
        <td> ${u.phoneNumber} </td>
        <td> ${u.birthDate} </td>
        <td> <img src="${u.photo}" width="64"> </td>
        <td>
	        <form:form action="/ecommerce/delete_user_submit" method="post" modelAttribute="user"> 
	        <form:input type="hidden" path="login" value="${u.login}" />
	        <form:input type="hidden" path="email" value="${u.email}" />
	        <form:input type="hidden" path="name" value="${u.name}" />
        	<input type="submit" value="Excluir" /> </form:form> 
       	</td>
    </tr>
</c:forEach>
</table>
</div>