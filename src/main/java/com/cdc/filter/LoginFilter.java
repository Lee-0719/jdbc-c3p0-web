package com.cdc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		Object isLogin = req.getSession().getAttribute("loginFlag");
		boolean isDoLogin = uri.endsWith("login.jsp") || uri.endsWith("LoginServlet") || isLogin != null;
		boolean isJsOrCss = uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".jar");
		boolean isStatic = uri.contains("/back/") || uri.endsWith("/basic/") || uri.endsWith("/images/") || uri.contains("/assets/");
		if(isDoLogin || isJsOrCss || isStatic) {
			chain.doFilter(req, res);
		}else {
			req.getRequestDispatcher("login.jsp").forward(req, res);
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
