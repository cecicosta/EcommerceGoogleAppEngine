<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>

<html>
<header>
<title> SmartVendas - Tudo de Smart pra voc&ecirc; </title>
	<style>
		a:link, a:visited {
		    color:				#27408B;
		    text-align: 		center;
		    text-decoration: 	none;
		    display: 			inline-block;
		}
		a:hover{
			color: 				white;
		}
		.header-footer {
			padding: 			5px 20px;
			font-family:    	Arial, Helvetica, sans-serif;
		}
		.menu-title {
		    font-family:    	Arial, Helvetica, sans-serif;
		    font-size:      	30px;
		    text-align: 		center;
		    padding: 			30px;
		}

		.menu {
		    font-family:    	Arial, Helvetica, sans-serif;
		    font-size:      	24px;
			padding: 			5px 20px;
		}
		.content {
		    font-family:    	Arial, Helvetica, sans-serif;
		    font-size:      	20px;
			padding: 			5px 30px;
		}
		table {
			border-collapse: 	collapse;
		}
		.table-content table,
		.table-content td {
			border: 			1px solid black;
			padding: 			6px 20px;
		}
		.search {
    		width: 				80%;
    		padding: 			12px 20px;
    		margin: 			8px 0;
    		border: 			2px solid #27408B;
    		border-radius: 		10px;
		}
		.search-button {
			background-color: 	#27408B;
		    border: 			none;
		    color: 				white;
		    font-size:      	20px;
		    padding: 			12px 24px;
		    margin: 			4px 4px;
		    border-radius: 		8px;
		}
		.form {
			padding: 			12px 20px;
    		margin: 			8px 0;
    		border: 			1px solid #27408B;
    		border-radius: 		10px;
		}
	</style>
</header>    
<body>
	<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr height="36px" bgcolor="#6CA6CD">
			<td class="header-footer"> Seja bem-vindo a SmartVendas</td>
			<td align="right" class="header-footer"> <a href="/ecommerce/login">Login</a></td>
		</tr>
		<tr>
			<td bgcolor="#BCD2EE" width="20%" valign="top"> <jsp:include page="./menu.jsp" /> </td>
			<td valign="top" align="left"> <jsp:include page="${mainPanel}"/></td>
		</tr>
		<tr height="36px" bgcolor="#6CA6CD">
			<td align="center" colspan="2" class="header-footer"> SmartCoisas todos os direitos reservados / Desenvolvimento de Software em Nuvem </td>
		</tr>
	</table>
</body>
</html>