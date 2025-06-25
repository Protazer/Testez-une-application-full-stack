package com.openclassrooms.starterjwt.unit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("AuthEntryPointJwt unit tests")
class AuthEntryPointJwtTest {

	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	AuthenticationException authException;

	@Test
	@DisplayName("it should test the security commence method")
	void shouldCommence() throws IOException, ServletException {
		// Arrange
		AuthEntryPointJwt entryPoint = new AuthEntryPointJwt();

		when(request.getServletPath()).thenReturn("/api/test");
		when(authException.getMessage()).thenReturn("Test unauthorized message");

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

		// Output stream creation and implement mandatory methods
		ServletOutputStream outputStream = new ServletOutputStream() {
			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setWriteListener(WriteListener writeListener) {

			}

			// write values into byteStream variable
			@Override
			public void write(int b) throws IOException {
				byteStream.write(b);
			}
		};

		when(response.getOutputStream()).thenReturn(outputStream);

		// Act
		entryPoint.commence(request, response, authException);

		// Assert
		verify(response).setContentType("application/json");
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ObjectMapper mapper = new ObjectMapper();
		var json = mapper.readValue(byteStream.toByteArray(), Map.class);

		assertEquals(401, json.get("status"));
		assertEquals("Unauthorized", json.get("error"));
		assertEquals("Test unauthorized message", json.get("message"));
		assertEquals("/api/test", json.get("path"));
	}
}
