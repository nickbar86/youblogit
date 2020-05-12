package com.youblog.blog.controllers;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AbstractConroller {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	protected static final String NO_USER_ID = "No User Id";
	
	protected Long getUserIdFromSC(SecurityContext sc) {
		if (sc != null && sc.getAuthentication() != null && sc.getAuthentication() instanceof JwtAuthenticationToken) {
			Jwt jwtToken = ((JwtAuthenticationToken) sc.getAuthentication()).getToken();
			return (Long) jwtToken.getClaims().get("userid");
		} else {
			return null;
		}
	}
	
	protected void logAuthorizationInfo(SecurityContext sc) {
		if (sc != null && sc.getAuthentication() != null && sc.getAuthentication() instanceof JwtAuthenticationToken) {
			Jwt jwtToken = ((JwtAuthenticationToken) sc.getAuthentication()).getToken();
			logAuthorizationInfo(jwtToken);
		} else {
			LOG.warn("No JWT based Authentication supplied, running tests are we?");
		}
	}

	private void logAuthorizationInfo(Jwt jwt) {
		if (jwt == null) {
			LOG.warn("No JWT supplied, running tests are we?");
		} else {
			if (LOG.isDebugEnabled()) {
				URL issuer = jwt.getIssuer();
				List<String> audience = jwt.getAudience();
				Object subject = jwt.getClaims().get("sub");
				Object scopes = jwt.getClaims().get("scope");
				Object expires = jwt.getClaims().get("exp");

				LOG.debug("Authorization info: Subject: {}, scopes: {}, expires {}: issuer: {}, audience: {}", subject,
						scopes, expires, issuer, audience);
			}
		}
	}
}
