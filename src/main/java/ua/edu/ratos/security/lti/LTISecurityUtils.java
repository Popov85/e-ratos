package ua.edu.ratos.security.lti;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LTISecurityUtils {

	public boolean isLMSUserWithOnlyLTIRole(final Authentication auth) {
		log.debug("Challenge whether it is an LMS user with single LTI role?");
		if (auth == null)
			return false;
		if (!auth.getPrincipal().getClass().equals(LTIToolConsumerCredentials.class))
			return false;
		if (auth.getAuthorities().size() != 1)
			return false;
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_LTI")))
			return false;
		return true;
	}

	public boolean isLMSUserWithLTIAndSTUDENTRoles(Authentication auth) {
		log.debug("Challenge whether it is an LMS user with both LTI & STUDENT roles?");
		if (auth == null)
			return false;
		if (!auth.getPrincipal().getClass().equals(LTIUserConsumerCredentials.class))
			return false;
		if (auth.getAuthorities().size() != 2)
			return false;
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_LTI"))
				|| !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT")))
			return false;
		return true;

	}

}
