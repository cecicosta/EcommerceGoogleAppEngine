<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib  uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="content">
<h2>Cadastrar Produto </h2>


<form:form action="/ecommerce/add_product_submit" method="post" enctype="multipart/form-data" modelAttribute="product">
<table>
	<tr>
		<td> Nome: </td>
		<td> <form:input type="text" path="name" placeholder="Nome do produto" required="required"/> </td>
	</tr>	
	<tr>
		<td> Fabricante: </td>
		<td> <form:input type="text" path="vendor" placeholder="Fabricante" required="required" /> </td>
	</tr>
	<tr>
		<td> Descri��o: </td>
		<td><form:input type="text" path="description" placeholder="Descri��o" rows="8" cols="40" required="required" /> </td>
	</tr>
		<tr>
		<td> Quantidade: </td>
		<td><form:input type="number" path="quantity" placeholder="Quantidade" size="10" min="1" step="1" required="required" /> </td>
	</tr>
	<tr>
		<td> Foto: (Max 3MB) </td>
		<td><form:input type="file" path="photoFile"/> </td>
	</tr>
	 
	<tr>
		<td colspan="2" align="center"> <input type="submit" value="Cadastrar"> </td>
	</tr>
</table>
</form:form>
</div>