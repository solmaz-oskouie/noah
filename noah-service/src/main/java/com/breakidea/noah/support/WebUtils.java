package com.breakidea.noah.support;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

public class WebUtils {

	/**
	 * add a cookie with special cookieName and cookieValue
	 *
	 * @param servletResponse
	 * @param cookieKey
	 * @param cookieValue
	 */
	public static void addCookie(HttpServletResponse response, String cookieKey, String cookieValue) {
		addCookie(response, cookieKey, cookieValue, false);
	}

	/**
	 * add a cookie with special cookieName and cookieValue
	 *
	 * @param response
	 * @param cookieKey
	 * @param cookieValue
	 */
	public static void addCookie(HttpServletResponse response, String cookieKey, String cookieValue, Boolean safeMode) {
		Assert.notNull(response, "HttpServletResponse must be provided");
		Assert.notNull(cookieKey, "cookieKey must be provided");

		Cookie targetCookie = new Cookie(cookieKey, cookieValue);
		if (safeMode) {
			try {
				targetCookie.setSecure(true);
				targetCookie.setHttpOnly(true);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		response.addCookie(targetCookie);
	}

	/**
	 * 简单数据绑定
	 *
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T bindRequest(HttpServletRequest request, Class<T> clazz) {
		Assert.notNull(request, "HttpServletRequest must be provided");
		Assert.notNull(clazz, "Class must be provided");

		T parameter = null;
		try {
			parameter = clazz.newInstance();
			ServletRequestDataBinder binder = new ServletRequestDataBinder(parameter);
			binder.bind(request);
		} catch (IllegalAccessException | InstantiationException e) {
			return null;
		}
		return parameter;
	}

	/**
	 * read a cookie with special cookieName
	 *
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Assert.notNull(request, "HttpServletRequest must be provided");
		Assert.notNull(cookieName, "cookieName must be provided");

		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookieName == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookieName.equalsIgnoreCase(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	/**
	 * 获取Cookie字符串的方法
	 *
	 * @param servletRequest
	 * @param cookieName
	 * @return
	 */
	public static String getCookieString(HttpServletRequest servletRequest, String cookieName) {
		Cookie cookies = getCookie(servletRequest, cookieName);
		if (ObjectUtils.isEmpty(cookies)) {
			return null;
		}
		return cookies.getValue();
	}

	public static String getParameter(HttpServletRequest request, String parameterName) {
		return request.getParameter(parameterName);
	}

	/**
	 * 获取前端异步提交的 payload 数据字串
	 *
	 * @param request
	 * @param inputCharset
	 * @return
	 */
	public static String getStream(HttpServletRequest request, Charset charset) {
		Assert.notNull(request, "HttpServletRequest must be provided");
		try {
			ServletInputStream body = request.getInputStream();
			if (ObjectUtils.isEmpty(body)) {
				return null;
			}
			return StreamUtils.copyToString(body, charset);
		} catch (IOException e) {

		}
		return null;
	}

	/**
	 * 判断当前请求是不是异步请求的方法
	 *
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		Assert.notNull(request, "HttpServletRequest must be provided");

		String xRequestedWith = request.getHeader("X-Requested-With");
		if (StringUtils.hasLength(xRequestedWith)) {
			return true;
		}
		return false;
	}
}
