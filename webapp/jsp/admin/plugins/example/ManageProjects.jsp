<jsp:useBean id="manageprojectProject" scope="session" class="fr.paris.lutece.plugins.example.web.ProjectJspBean" />
<% String strContent = manageprojectProject.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
