<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.example.web.ManageProjectJspBean"%>

${ ProjectJspBean.init( pageContext.request, ProjectJspBean.RIGHT_MANAGE_DUMMY ) }
${ ProjectJspBean.getManageDaemons( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>