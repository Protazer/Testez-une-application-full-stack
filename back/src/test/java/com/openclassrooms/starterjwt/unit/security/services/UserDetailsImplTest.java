package com.openclassrooms.starterjwt.unit.security.services;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplTest {

	private UserDetailsImpl userDetails;

	@BeforeEach
	public void setup() {
		userDetails = UserDetailsImpl.builder()
				.id(1L)
				.username("test@studio.com")
				.firstName("userFirstName")
				.lastName("userLastName")
				.admin(false)
				.password("test!1234")
				.build();
	}


	@Test
	void shouldTestTheBuilder() {
		UserDetailsImpl buildUser = UserDetailsImpl.builder()
				.id(2L)
				.username("test2@studio.com")
				.firstName("userFirstName2")
				.lastName("userLastName2")
				.admin(false)
				.password("test!1234")
				.build();

		assertThat(buildUser).isNotNull();
		assertEquals(2L, buildUser.getId());
		assertEquals("test2@studio.com", buildUser.getUsername());
		assertEquals("userFirstName2", buildUser.getFirstName());
		assertEquals("userLastName2", buildUser.getLastName());
		assertEquals("test!1234", buildUser.getPassword());
		assertFalse(buildUser.getAdmin());
	}

	@Test
	void getAuthorities() {
		//Assert
		assertNotNull(userDetails.getAuthorities());
		assertTrue(userDetails.getAuthorities().isEmpty());
	}

	@Test
	void isAccountNonExpired() {
		//Assert
		assertTrue(userDetails.isAccountNonExpired());
	}

	@Test
	void isAccountNonLocked() {
		//Assert
		assertTrue(userDetails.isAccountNonLocked());
	}

	@Test
	void isCredentialsNonExpired() {
		//Assert
		assertTrue(userDetails.isCredentialsNonExpired());
	}

	@Test
	void isEnabled() {
		//Assert
		assertTrue(userDetails.isEnabled());
	}

	@Test
	void testEquals() {
		//Assert
		assertThat(userDetails.equals(userDetails)).isTrue();
	}

	@Test
	void testEqualsWithDifferentObject() {
		//Arrange
		UserDetailsImpl mockedDifferentUser = UserDetailsImpl.builder()
				.id(2L)
				.username("test2@studio.com")
				.firstName("userFirstName2")
				.lastName("userLastName2")
				.admin(true)
				.password("test!12342")
				.build();

		//Assert
		assertThat(userDetails.equals(mockedDifferentUser)).isFalse();
	}

	@Test
	void testEqualsWithNoDetailsObject() {
		//Arrange
		Object nonUserDetailsObject = new Object();
		//Assert
		assertThat(userDetails.equals(nonUserDetailsObject)).isFalse();

	}

	@Test
	void testEqualsWithNull() {
		//Assert
		assertThat(userDetails.equals(null)).isFalse();

	}

	@Test
	void getId() {
		//Assert
		assertNotNull(userDetails.getId());
		assertEquals(1L, userDetails.getId());
	}

	@Test
	void getUsername() {
		//Assert
		assertNotNull(userDetails.getUsername());
		assertEquals("test@studio.com", userDetails.getUsername());
	}

	@Test
	void getFirstName() {
		//Assert
		assertNotNull(userDetails.getFirstName());
		assertEquals("userFirstName", userDetails.getFirstName());
	}

	@Test
	void getLastName() {
		//Assert
		assertNotNull(userDetails.getLastName());
		assertEquals("userLastName", userDetails.getLastName());
	}

	@Test
	void getAdmin() {
		//Assert
		assertNotNull(userDetails.getAdmin());
		assertFalse(userDetails.getAdmin());
	}

	@Test
	void getPassword() {
		//Assert
		assertNotNull(userDetails.getPassword());
		assertEquals("test!1234", userDetails.getPassword());
	}


}