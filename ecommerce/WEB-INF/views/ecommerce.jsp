<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="content">
<h2 align="center"> Carrinho de Compras</h2>
<table class="table-content">
    <tr>
        <td> <b>Nome</b> </td>
        <td> <b>Fabricante</b> </td>
        <td> <b>Descricao</b> </td>
        <td> <b>Foto</b> </td>
    </tr>
	
<c:forEach var="p" items="${productList}">
	<!-- Para cada produto, repetir o código entre o <tr> </tr> abaixo -->
    <tr>
        <td> ${p.name} </td>
        <td> ${p.vendor} </td>
        <td> ${p.description} </td>
        <td> <img src="${p.photo}" width="64"> </td>
        <td>
	        <form:form action="/ecommerce/select_product" method="post" modelAttribute="product"> 
	        <form:input type="hidden" path="name" value="${p.name}" />
	        <form:input type="hidden" path="vendor" value="${p.vendor}" />
	        <form:input type="hidden" path="description" value="${p.description}" />
        	<input type="submit" value="Adicionar ao carrinho" /> </form:form> 
       	</td>
    </tr>
</c:forEach>

</table>
</div>