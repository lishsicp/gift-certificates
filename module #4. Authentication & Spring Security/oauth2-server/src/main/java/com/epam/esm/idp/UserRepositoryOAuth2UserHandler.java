package com.epam.esm.idp;

import com.epam.esm.entity.AuthUser;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.AuthUserRepository;
import com.epam.esm.repository.AuthUserRoleRepository;
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
public class UserRepositoryOAuth2UserHandler implements Consumer<OAuth2User> {

	private final AuthUserRepository userRepository;
	private final AuthUserRoleRepository userRoleRepository;

	@Override
	public void accept(OAuth2User user) {
		if (!userRepository.existsByEmail(user.getName())) {
			String nameAttribute = user.getAttribute("name");
			if (nameAttribute != null) {
				String[] fullName = nameAttribute.split("\\s");
				var authUser = AuthUser.builder()
					.email(user.getAttribute("email"))
					.firstname(fullName[0])
					.lastname(fullName[1])
					.password(Sha512DigestUtils.shaHex(RandomStringUtils.randomAlphabetic(10))) // random password
					.role(userRoleRepository
						.findByName("USER")
						.orElseThrow(() -> new EntityNotFoundException("User role does not exist")))
					.build();
				log.info("Saving new user: name=" + user.getName() + ", attributes=" + user.getAttributes() + ", authorities=" + user.getAuthorities());
				this.userRepository.save(authUser);
			}
		}
	}
}
