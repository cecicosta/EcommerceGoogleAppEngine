<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<div class="content" align="center">
<h2 align="center"> Excluir Produto </h2>
<form action="/delete_product_action.jsp" method="post">

<input type="submit" value="Excluir" class="search-button"> <br /><br />
<table class="table-content" width="100%">
    <tr>
        <td> <b>Select</b> </td>
        <td> <b>Nome</b> </td>
        <td> <b>Fabricante</b> </td>
        <td> <b>Descricao</b> </td>
        <td> <b>Qnt.</b> </td>
        <td> <b>Foto</b> </td>
    </tr>
        <!-- Para cada produto, repetir o código entre o <tr> </tr> abaixo -->
    <tr>
        <!-- O id do produto deve estar no campo value pra o form retornar ele quando for selecionado / O campo name deve ser o mesmo para todos os radio buttons -->
        <td> <input type="radio" name="user_id" value="product_id_from_database"> </td>
        <td> <%-- @nome_do_produto --%> </td>
        <td> <%-- @fabricante_do_produto --%> </td>
        <td> <%-- @descricao_do_produto --%> </td>
        <td> <%-- @quantidade_do_produto --%> </td>
        <td> <img src="/caminho_da_imagem.jpg"> </td>
    </tr>

</table>
</form>
</div>