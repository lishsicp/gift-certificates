package com.epam.esm.jose;

import lombok.experimental.UtilityClass;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

@UtilityClass
final class KeyGeneratorUtils {

	static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}
}