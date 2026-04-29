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
        System.out.println("=== Testler başlıyor ===");
    }

    @AfterAll
    static void tearDownSuite() {
        System.out.println("=== Testler bitti ===");
    }

    @Test
    @Order(1)
    @DisplayName("TC-01 | Geçerli bilgiler - kayıt başarılı")
    void testSubmit_allValidInputs_shouldSucceed() {
        assertEquals("Registration successful!", form.submit());
    }

    @Test
    @Order(2)
    @DisplayName("TC-02 | Ad null - hata vermeli")
    void testFirstName_null_shouldThrow() {
        form = new RegistrationForm(null, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, form::submit);
        assertTrue(ex.getMessage().contains("First name cannot be empty"));
    }

    @Test
    @Order(3)
    @DisplayName("TC-03 | Ad 1 karakter - hata vermeli (BVA)")
    void testFirstName_oneChar_shouldThrow() {
        form = new RegistrationForm("A", VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(4)
    @DisplayName("TC-04 | Ad 2 karakter - geçerli (BVA)")
    void testFirstName_twoChars_shouldSucceed() {
        form = new RegistrationForm("Al", VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertDoesNotThrow(form::submit);
    }

    @Test
    @Order(5)
    @DisplayName("TC-05 | Ad 51 karakter - hata vermeli (BVA)")
    void testFirstName_51Chars_shouldThrow() {
        form = new RegistrationForm("A".repeat(51), VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(6)
    @DisplayName("TC-06 | Ad rakam içeriyor - hata vermeli")
    void testFirstName_containsDigits_shouldThrow() {
        form = new RegistrationForm("Alice123", VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(7)
    @DisplayName("TC-07 | Email @ işareti yok - hata vermeli")
    void testEmail_missingAt_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, "aliceexample.com", VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(8)
    @DisplayName("TC-08 | Email boşluk içeriyor - hata vermeli")
    void testEmail_withSpace_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, "alice @example.com", VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(9)
    @DisplayName("TC-09 | Email çift @ işareti - hata vermeli")
    void testEmail_doubleAt_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, "alice@@example.com", VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(10)
    @DisplayName("TC-10 | Şifre 7 karakter - hata vermeli (BVA)")
    void testPassword_7Chars_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "Sec@123", "Sec@123", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(11)
    @DisplayName("TC-11 | Şifre büyük harf yok - hata vermeli")
    void testPassword_noUppercase_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "secure@123", "secure@123", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(12)
    @DisplayName("TC-12 | Şifre rakam yok - hata vermeli")
    void testPassword_noDigit_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "Secure@Pass", "Secure@Pass", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(13)
    @DisplayName("TC-13 | Şifre özel karakter yok - hata vermeli")
    void testPassword_noSpecialChar_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, "Secure1234", "Secure1234", VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(14)
    @DisplayName("TC-14 | Şifreler eşleşmiyor - hata vermeli")
    void testConfirmPassword_mismatch_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, "Different@99", VALID_DOB);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, form::submit);
        assertTrue(ex.getMessage().contains("do not match"));
    }

    @Test
    @Order(15)
    @DisplayName("TC-15 | Doğum tarihi yanlış format - hata vermeli")
    void testDOB_wrongFormat_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, "06-15-1995");
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(16)
    @DisplayName("TC-16 | 13 yaşından küçük - hata vermeli (BVA)")
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
    @DisplayName("TC-17 | Tam 13 yaşında - geçerli (BVA)")
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
    @DisplayName("TC-18 | Geçersiz tarih formatı - hata vermeli")
    void testDOB_impossibleDate_shouldThrow() {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, VALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD, "abc/def/ghij");
        assertThrows(IllegalArgumentException.class, form::submit);
    }

    @Test
    @Order(19)
    @DisplayName("TC-19 | Soyadı tire içeriyor - geçerli")
    void testLastName_hyphenated_shouldSucceed() {
        form = new RegistrationForm(VALID_FIRST, "Smith-Jones", VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertDoesNotThrow(form::submit);
    }

    @ParameterizedTest(name = "TC-20 | Geçersiz email: {0}")
    @Order(20)
    @ValueSource(strings = {"", "noatsign", "@nodomain", "user@", "user@.com", "user@domain."})
    void testEmail_variousInvalidFormats_shouldThrow(String invalidEmail) {
        form = new RegistrationForm(VALID_FIRST, VALID_LAST, invalidEmail, VALID_PASSWORD, VALID_PASSWORD, VALID_DOB);
        assertThrows(IllegalArgumentException.class, form::submit);
    }
}