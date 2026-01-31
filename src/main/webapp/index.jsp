<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/auth?action=login"/>
</c:if>

<%-- Set layout attributes and forward to layout --%>
<c:set var="pageTitle" value="Dashboard" scope="request"/>
<c:set var="contentPage" value="/views/dashboard/content.jsp" scope="request"/>
<jsp:forward page="/WEB-INF/views/common/layout.jsp"/>