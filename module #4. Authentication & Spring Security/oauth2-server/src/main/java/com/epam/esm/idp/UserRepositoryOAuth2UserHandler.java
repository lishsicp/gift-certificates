package com.epam.esm.idp;

import com.epam.esm.entity.AuthUser;
import com.epam.esm.entity.Role;
import com.epam.esm.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Component
@Slf4j
public final class UserRepositoryOAuth2UserHandler implements Consumer<OAuth2User> {

	private final AuthUserRepository userRepository;

	@Override
	public void accept(OAuth2User user) {
		if (!userRepository.existsByEmail(user.getName())) {

			var authUser = AuthUser.builder()
				.email(user.getAttribute("email"))
				.firstname(user.getAttribute("given_name"))
				.lastname(user.getAttribute("family_name"))
				.password(Sha512DigestUtils.shaHex(RandomStringUtils.randomAlphabetic(10))) // random password
				.role(Role.USER)
				.build();
			log.info("Saving new user: name=" + user.getName() + ", attributes=" + user.getAttributes() + ", authorities=" + user.getAuthorities());
			this.userRepository.save(authUser);
		}
	}
}
