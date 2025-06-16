package com.openclassrooms.starterjwt.unit.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

	@InjectMocks
	JwtUtils jwtUtils;
	@Mock
	UserDetailsImpl userDetails;
	@Mock
	Authentication authentication;

	@BeforeEach
	public void setup() throws NoSuchFieldException, IllegalAccessException {
		Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
		secretField.setAccessible(true);
		String secretKey = "secretKey";
		secretField.set(jwtUtils, secretKey);

		Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
		expirationField.setAccessible(true);
		int expirationTime = 360000;
		expirationField.set(jwtUtils, expirationTime);

		userDetails = new UserDetailsImpl(1L, "test@studio.com", "userFistName", "userLastName", false, "userPassword");
	}

	@Test
	void generateJwtToken() {
		//Arrange
		when(authentication.getPrincipal()).thenReturn(userDetails);
		//Act
		String token = jwtUtils.generateJwtToken(authentication);
		//Assert
		assertNotNull(token);
	}

	@Test
	void shouldGetUserNameFromJwtToken() {
		//Arrange
		when(authentication.getPrincipal()).thenReturn(userDetails);
		String token = jwtUtils.generateJwtToken(authentication);
		String userName = jwtUtils.getUserNameFromJwtToken(token);
		//Assert
		assertEquals("test@studio.com", userName);

	}

	@Test
	void shouldValidateJwtToken() {
		when(authentication.getPrincipal()).thenReturn(userDetails);

		String token = jwtUtils.generateJwtToken(authentication);

		assertTrue(jwtUtils.validateJwtToken(token));
	}

	@Test
	void shouldReturnInvalidToken() {
		String invalidToken = "invalid token";
		assertFalse(jwtUtils.validateJwtToken(invalidToken));
	}

	@Test
	void shouldReturnInvalidTokenWithEmptyValue() {
		assertFalse(jwtUtils.validateJwtToken(""));
	}

	@Test
	void shouldReturnInvalidSignatureToken() {
		String invalidSignatureToken = Jwts.builder()
				.setSubject("test@studio.com")
				.setIssuedAt(new Date(System.currentTimeMillis() - 10000)) // il y a 10 sec
				.setExpiration(new Date(System.currentTimeMillis() - 5000)) // expiré il y a 5 sec
				.signWith(SignatureAlgorithm.HS512, "invalidSignature")
				.compact();

		assertFalse(jwtUtils.validateJwtToken(invalidSignatureToken));
	}

	@Test
	void shouldReturnInvalidExpiredToken() {
		String expiredToken = Jwts.builder()
				.setSubject("test@studio.com")
				.setIssuedAt(new Date(System.currentTimeMillis() - 10000)) // il y a 10 sec
				.setExpiration(new Date(System.currentTimeMillis() - 5000)) // expiré il y a 5 sec
				.signWith(SignatureAlgorithm.HS512, "secretKey")
				.compact();

		assertFalse(jwtUtils.validateJwtToken(expiredToken));
	}

	@Test
	void shouldReturnUnsupportedToken() {
		String unsupportedToken = Jwts.builder()
				.setPayload("bad payload")
				.compact();

		assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
	}
}