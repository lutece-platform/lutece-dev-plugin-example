<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="managebp" scope="session" class="fr.paris.lutece.plugins.bp.web.ManageBpJspBean" />

<% managebp.init( request, managebp.RIGHT_MANAGEBP ); %>
<%= managebp.getManageBpHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
