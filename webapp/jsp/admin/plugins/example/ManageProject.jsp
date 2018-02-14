<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="manageproject" scope="session" class="fr.paris.lutece.plugins.example.web.ManageProjectJspBean" />

<% manageproject.init( request, manageproject.RIGHT_MANAGEPROJECT ); %>
<%= manageproject.getManageProjectHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
