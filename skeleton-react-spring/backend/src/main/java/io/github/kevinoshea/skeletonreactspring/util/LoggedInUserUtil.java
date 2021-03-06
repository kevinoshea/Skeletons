package io.github.kevinoshea.skeletonreactspring.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import io.github.kevinoshea.skeletonreactspring.model.LoginType;
import io.github.kevinoshea.skeletonreactspring.model.User;

public class LoggedInUserUtil {

	public static User getLoggedInUser() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof OAuth2AuthenticationToken) {
			final OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
			final OAuth2User principal = token.getPrincipal();

			final User user = new User();
			user.setSocialId(principal.getName());
			user.setLoginType(LoginType.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase()));

			user.setUserId(String.format("%s-%s", user.getLoginType(), principal.getName()));
			user.setSocialLogin(principal.getAttribute("login"));
			user.setDisplay(principal.getAttribute("name"));

			return user;
		}

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
			final LdapUserDetails principal = (LdapUserDetails) token.getPrincipal();

			final User user = new User();
			user.setLoginType(LoginType.LDAP);
			user.setUserId("ldap-" + principal.getUsername());
			user.setSocialId(principal.getUsername());
			user.setSocialLogin(principal.getUsername());
			user.setDisplay(principal.getUsername());

			return user;
		}

		return null;
	}
}
