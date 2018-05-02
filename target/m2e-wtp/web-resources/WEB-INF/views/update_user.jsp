<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib  uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="content">
<h2>Atualizar Usu�rio </h2>
<form:form action="/ecommerce/add_user_submit?update=true" method="post" enctype="multipart/form-data" modelAttribute="user">
<table>
	<tr>
		<td> <form:input type="hidden" path="login" placeholder="Nome de usuario" value="${authUser.login}"/> </td>
	</tr>	
	<tr>
		<td> Senha: </td>
		<td> <form:input type="password" path="password" placeholder="Senha"  required="required" /> </td>
	</tr>
	<tr>
		<td> Email: </td>
		<td><form:input type="email" path="email" placeholder="Email" value="${authUser.email}" required="required" /> </td>
	</tr>
		<tr>
		<td> Nome Completo: </td>
		<td><form:input type="text" path="name" placeholder="Nome" value="${authUser.name}" required="required" /> </td>
	</tr>
		<tr>
		<td> Data de Nascimento: </td>
		<td><form:input type="date" path="birthDate" placeholder="Data de nascimento" value="${authUser.birthDate}" required="required" /> </td>
	</tr>
	<tr>
		<td> Telefone: </td>
		<td><form:input type="text" path="phoneNumber" placeholder="(99) 99999-9999" value="${authUser.phoneNumber}" required="required" /> </td>
	</tr>
	<tr>
		<td><form:input type="hidden" path="photo" value="${authUser.photo}" required="required" /> </td>
	</tr>
	<tr>
		<td> Foto: (Max 3MB) </td>
		<td><form:input type="file" path="photoFile"/> </td>
	</tr>
	 
	<tr>
		<td colspan="2" align="center"> <input type="submit" value="Atualizar"> </td>
	</tr>
</table>
</form:form>
</div>