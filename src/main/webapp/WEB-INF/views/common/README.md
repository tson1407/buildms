# Common Layout System

## Overview
The common layout system provides a centralized template structure for all JSP pages in the Smart WMS application.

## File Structure
```
/WEB-INF/views/common/
  ├── layout.jsp    - Master layout (contains <!DOCTYPE>, <html>, <head>, <body>)
  ├── header.jsp    - Wrapper start + sidebar + navbar (for authenticated pages)
  └── footer.jsp    - Wrapper end + scripts (for authenticated pages)
```

## Usage

### For Main Application Pages (with sidebar/navbar)
Content pages should ONLY contain the page-specific content wrapped in proper container structure.

**Example: /views/product/list-content.jsp**
```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Content -->
<div class="container-xxl flex-grow-1 container-p-y">
    <h4 class="fw-bold">Products</h4>
    
    <!-- Your page content here -->
    <div class="card">
        <div class="card-body">
            <!-- Content -->
        </div>
    </div>
</div>
<!-- / Content -->
```

**Servlet forwarding:**
```java
// Set page metadata
request.setAttribute("pageTitle", "Products");
request.setAttribute("contentPage", "/views/product/list-content.jsp");

// Optional: add custom CSS/JS
request.setAttribute("pageCss", "/assets/vendor/css/pages/page-products.css");
request.setAttribute("pageJs", "/assets/js/pages/products.js");

// Forward to layout
request.getRequestDispatcher("/WEB-INF/views/common/layout.jsp").forward(request, response);
```

### For Authentication Pages (no sidebar/navbar)
**Example: /views/auth/login-content.jsp**
```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Content -->
<div class="container-xxl">
    <div class="authentication-wrapper authentication-basic container-p-y">
        <div class="authentication-inner">
            <!-- Login form -->
        </div>
    </div>
</div>
<!-- / Content -->
```

**Servlet forwarding:**
```java
// Set page metadata
request.setAttribute("pageTitle", "Login");
request.setAttribute("layoutType", "auth");
request.setAttribute("layoutClass", "layout-wide customizer-hide");
request.setAttribute("contentPage", "/views/auth/login-content.jsp");
request.setAttribute("pageCss", "/assets/vendor/css/pages/page-auth.css");

// Forward to layout
request.getRequestDispatcher("/WEB-INF/views/common/layout.jsp").forward(request, response);
```

## Layout Variables

### Required
- `contentPage` (String) - Path to the content JSP file

### Optional
- `pageTitle` (String) - Browser tab title (default: "Dashboard")
- `layoutType` (String) - "auth" for authentication pages, omit for main pages
- `layoutClass` (String) - HTML class attribute (default: "layout-menu-fixed layout-compact")
- `pageCss` (String) - Path to additional CSS file
- `pageJs` (String) - Path to additional JS file

## Benefits
1. **Single source of truth** - HTML structure defined once in layout.jsp
2. **No duplication** - No repeated DOCTYPE, head, scripts across pages
3. **Consistent styling** - All pages use same CSS/JS loading order
4. **Easy maintenance** - Change layout once, affects all pages
5. **Clean content pages** - Content JSPs focus only on their specific markup

## Migration Guide
To migrate existing JSP pages:

1. Remove all HTML boilerplate (<!DOCTYPE>, <html>, <head>, <body>)
2. Remove header/footer includes
3. Keep only the content inside `<div class="container-xxl flex-grow-1 container-p-y">`
4. Update servlet to set `contentPage` and forward to layout.jsp
5. Move page-specific CSS/JS to `pageCss`/`pageJs` attributes

## Rules
- ✅ layout.jsp is the ONLY file with <!DOCTYPE>, <html>, <head>, <body>
- ✅ Use <jsp:include> for modular components
- ✅ Use EL/JSTL only - NO SCRIPTLETS
- ✅ Content pages contain ONLY their specific markup
- ❌ Never duplicate HTML structure in content pages
