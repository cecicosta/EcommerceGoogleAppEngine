<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<div class="content" align="center">
<h2 align="center"> Excluir Usu&aacute;rio </h2>
<form action="/delete_user_action.jsp" method="post">

<input type="submit" value="Excluir" class="search-button"><br /><br />

<table class="table-content" width="100%">
    <tr>
        <td> <b>Select</b> </td>
        <td> <b>Login</b> </td>
        <td> <b>Nome</b> </td>
        <td> <b>E-mail</b> </td>
        <td> <b>Telefone</b> </td>
        <td> <b>Nascimento</b> </td>
        <td> <b>Foto</b> </td>
    </tr>
        <!-- Para cada produto, repetir o código entre o <tr> </tr> abaixo -->
    <tr>
        <!-- O id do usuario deve estar no campo value pra o form retornar ele quando for selecionado / O campo name deve ser o mesmo para todos os radio buttons -->
        <td> <input type="radio" name="user_id" value="user_id_from_database"> </td>
        <td> <%-- @login_do_usuario --%> </td>
        <td> <%-- @nome_do_usuario --%> </td>
        <td> <%-- @email_do_usuario --%> </td>
        <td> <%-- @telefone_do_usuario --%> </td>
        <td> <%-- @nascimento_do_usuario --%> </td>
        <td> <img src="/caminho_da_imagem.jpg"> </td>
    </tr>

</table>
</form>
</div>