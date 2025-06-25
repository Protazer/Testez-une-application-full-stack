package com.openclassrooms.starterjwt.unit.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthTokenFilter unit tests")
class AuthTokenFilterTest {

	@Mock
	private UserDetailsServiceImpl userDetailsService;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private FilterChain filterChain;

	@InjectMocks
	private AuthTokenFilter authTokenFilter;

	private MockHttpServletRequest mockHttpServletRequest;

	private MockHttpServletResponse mockHttpServletResponse;

	@BeforeEach
	public void setup() {
		mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletResponse = new MockHttpServletResponse();
	}

	@Test
	@DisplayName("it should test authentication with valid token")
	void shouldDoFilterInternal() throws ServletException, IOException {
		//Arrange
		String token = "testToken";
		String userName = "test@studio.com";
		mockHttpServletRequest.addHeader("Authorization", "Bearer " + token);
		UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@studio.com", "testFirstname", "testLastName", false, "test!1234");
		when(jwtUtils.validateJwtToken(token)).thenReturn(true);
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(userName);
		when(userDetailsService.loadUserByUsername(userName)).thenReturn(userDetails);
		//Act
		authTokenFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, filterChain);
		//Assert
		verify(jwtUtils).validateJwtToken(token);
		verify(jwtUtils).getUserNameFromJwtToken(token);
		verify(userDetailsService).loadUserByUsername(userName);
		verify(filterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
	}

	@Test
	@DisplayName("it should test authentication with invalid token format")
	void doFilterInternalWithInvalidTokenFormat() throws ServletException, IOException {
		String token = "testToken";
		mockHttpServletRequest.addHeader("Authorization", "Bearer " + token);
		when(jwtUtils.validateJwtToken(token)).thenReturn(false);

		authTokenFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, filterChain);
		verify(jwtUtils).validateJwtToken(token);
		verify(filterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
	}

	@Test
	@DisplayName("it should test authentication with invalid token value")
	void shouldDoFilterInternalWithInvalidJwt() throws ServletException, IOException {
		mockHttpServletRequest.addHeader("Authorization", "abcd");
		authTokenFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, filterChain);
		verify(filterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
	}

	@Test
	@DisplayName("should test the runtime exception if getUserName return an exception")
	void shouldDoFilterInternalCatchException() throws ServletException, IOException {
		String token = "testToken";

		mockHttpServletRequest.addHeader("Authorization", "Bearer " + token);

		when(jwtUtils.validateJwtToken(token)).thenReturn(true);
		when(jwtUtils.getUserNameFromJwtToken(token)).thenThrow(new RuntimeException("Cannot set user authentication: {}"));

		authTokenFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, filterChain);
		verify(filterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);

	}

}