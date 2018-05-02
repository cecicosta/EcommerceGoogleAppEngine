<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="content">
<h2 align="center"> Log de Opera��es</h2>
<table class="table-content">

    <tr>
        <td> <b>Usu�rio</b> </td>
        <td> <b>Opera��o</b> </td>
        <td> <b>Tipo</b> </td>
        <td> <b>Data</b> </td>
 	</tr>
 	
<c:forEach var="l" items="${logList}">
	<!-- Para cada produto, repetir o código entre o <tr> </tr> abaixo -->
    <tr>
        <td> ${l.user} </td>
        <td> ${l.operation} </td>
        <td> ${l.affectedType} </td>
        <td> ${l.time} </td>
    </tr>
</c:forEach>
</table>
</div>