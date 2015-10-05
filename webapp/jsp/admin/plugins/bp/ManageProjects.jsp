<jsp:useBean id="managebpProject" scope="session" class="fr.paris.lutece.plugins.bp.web.ProjectJspBean" />
<% String strContent = managebpProject.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
