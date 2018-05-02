<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style>
.btn-group button {
    padding: 10px 24px; /* Some padding */
    cursor: pointer; /* Pointer/hand icon */
    float: left; /* Float the buttons side by side */
}

.btn-group input {
    padding: 10px 20px; /* Some padding */
    cursor: pointer; /* Pointer/hand icon */
    float: left; /* Float the buttons side by side */
}
</style>

<div class="content">
<h2 align="center"> Online Store SmartVendas</h2>
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
    </tr>
</c:forEach>
</table>

<div class="btn-group">
  		<form:form style="vertical-align: middle;" action="/ecommerce/finish_purchase" method="post" > 
		<input type="submit" value="Finalizar Compra" /> </form:form>
		<form:form style="vertical-align: middle;" action="/ecommerce/finish_purchase?			action=clear_shopping_cart" method="post" > 
		<input type="submit" value="Limpar Carrinho" /></form:form> 
        <a href="/ecommerce/product_menu?action=ecommerce" target="_parent">
		<button>Adicionar Outros</button></a>
</div>
</div>