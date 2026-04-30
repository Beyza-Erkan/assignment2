package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegistrationFormTest {

    private static final String VALID_FIRST    = "Alice";
    private static final String VALID_LAST     = "Smith";
    private static final String VALID_EMAIL    = "alice@example.com";
    private static final String VALID_PASSWORD = "Secure@123";
    private static final String VALID_DOB      = "15/06/1995";

    private RegistrationForm form;

    @BeforeEach
    void setUp() {
        form = new RegistrationForm(
                VALID_FIRST, VALID_LAST,
                VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD,
                VALID_DOB);
    }

    @AfterEach
    void tearDown() {
        form = null;
    }

    @BeforeAll
    static void initSuite() {
        System.out.println("=== Test suite starting ===");
    }

    @AfterAll
    static void tearDownSuite() {
        System.out.println("=== Test suite finished ===");
    }

    @Test
    @Order(1)
    @DisplayName("TC-01 | All valid inputs - registration succeeds")
    void testSubmit_allValidInputs_shouldSucceed() {
        assertEquals("Registration successful!", form.submit());
    }

    @Test
    @Order(2)
    @DisplayName("TC-02 | First name null - should throw")
    void testFirstName_null_shouldThrow() {
        form = new RegistrationForm(null, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, form::submit);
        assertTrue(ex.getMessage().contains("First name cannot be empty"));
    }

    @Test
    @Order(3)
    @DisplayName("TC-03 | First name 1 char - should throw (BVA min-1)")
    void testFirstName_oneChar_shouldThrow() {
        form = new RegistrationForm("A", VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(4)
    @DisplayName("TC-04 | First name 2 chars - should succeed (BVA min)")
    void testFirstName_twoChars_shouldSucceed() {
        form = new RegistrationForm("Al", VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertDoesNotThrow(form::submit);
    }

    @Test
    @Order(5)
    @DisplayName("TC-05 | First name 51 chars - should throw (BVA max+1)")
    void testFirstName_51Chars_shouldThrow() {
        form = new RegistrationForm("A".repeat(51), VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(6)
    @DisplayName("TC-06 | First name contains digits - should throw")
    void testFirstName_containsDigits_shouldThrow() {
        form = new RegistrationForm("Alice123", VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(7)
    @DisplayName("TC-07 | Email missing @ symbol - should throw")
    void testEmail_missingAt_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, "aliceexample.com", VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(8)
    @DisplayName("TC-08 | Email contains space - should throw")
    void testEmail_withSpace_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, "alice @example.com", VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(9)
    @DisplayName("TC-09 | Email double @@ - should throw")
    void testEmail_doubleAt_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, "alice@@example.com", VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(10)
    @DisplayName("TC-10 | Password 7 chars - should throw (BVA min-1)")
    void testPassword_7Chars_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "Sec@123", "Sec@123", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(11)
    @DisplayName("TC-11 | Password no uppercase - should throw")
    void testPassword_noUppercase_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "secure@123", "secure@123", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(12)
    @DisplayName("TC-12 | Password no digit - should throw")
    void testPassword_noDigit_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "Secure@Pass", "Secure@Pass", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(13)
    @DisplayName("TC-13 | Password no special character - should throw")
    void testPassword_noSpecialChar_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "Secure1234", "Secure1234", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(14)
    @DisplayName("TC-14 | Passwords do not match - should throw")
    void testConfirmPassword_mismatch_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, "Different@99", VALID_DOB);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, form::submit);
        assertTrue(ex.getMessage().contains("do not match"));
    }

    @Test
    @Order(15)
    @DisplayName("TC-15 | Date of birth wrong format - should throw")
    void testDOB_wrongFormat_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, "06-15-1995");
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(16)
    @DisplayName("TC-16 | Age under 13 - should throw (BVA)")
    void testDOB_under13_shouldThrow() {
        LocalDate tenYearsAgo = LocalDate.now().minusYears(10);
        String dob = String.format("%02d/%02d/%04d",
                tenYearsAgo.getDayOfMonth(),
                tenYearsAgo.getMonthValue(),
                tenYearsAgo.getYear());
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, dob);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(17)
    @DisplayName("TC-17 | Age exactly 13 - should succeed (BVA)")
    void testDOB_exactly13_shouldSucceed() {
        LocalDate exactly13 = LocalDate.now().minusYears(13);
        String dob = String.format("%02d/%02d/%04d",
                exactly13.getDayOfMonth(),
                exactly13.getMonthValue(),
                exactly13.getYear());
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, dob);
        assertDoesNotThrow(form::submit);
    }

    @Test
    @Order(18)
    @DisplayName("TC-18 | Invalid date string - should throw")
    void testDOB_impossibleDate_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, "abc/def/ghij");
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(19)
    @DisplayName("TC-19 | Hyphenated last name - should succeed")
    void testLastName_hyphenated_shouldSucceed() {
        form = new RegistrationForm(VALID_FIRST, "Smith-Jones", VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertDoesNotThrow(form::submit);
    }

    @ParameterizedTest(name = "TC-20 | Invalid email: \"{0}\" - should throw")
    @Order(20)
    @ValueSource(strings = {"", "noatsign", "@nodomain", "user@", "user@.com", "user@domain."})
    void testEmail_variousInvalidFormats_shouldThrow(String invalidEmail) {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, invalidEmail, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }
}