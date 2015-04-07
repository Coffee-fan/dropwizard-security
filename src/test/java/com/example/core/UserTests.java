package com.example.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class UserTests {

    private static final String NULL_ERROR_MESSAGE = "may not be null";
    private static final String EMPTY_ERROR_MESSAGE = "may not be empty";

    private static Validator validator;
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    /**
     * Loads the given fixture resource as a normalized JSON string.
     *
     * @param filename    the filename of the fixture
     * @return the contents of {@code filename} as a normalized JSON string
     * @throws IOException if there is an error parsing {@code filename}
     */
    private static String jsonFixture(String filename) throws IOException {
        return MAPPER.writeValueAsString(MAPPER.readValue(fixture(filename), JsonNode.class));
    }

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void serializesToJson() throws Exception {
        final User user = getUser();
        assertThat(MAPPER.writeValueAsString(user)).isEqualTo(jsonFixture("fixtures/user.json"));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final User user = getUser();
        assertThat(MAPPER.readValue(fixture("fixtures/user.json"), User.class)).isEqualToComparingFieldByField(user);
    }

    // Should be replaced with individual class field validator tests
    @Test
    public void validate_not_null_or_empty() throws Exception {
        User user = new User();

        Set<ConstraintViolation<User>> constraintViolations =
                validator.validate(user);

        assertEquals(4, constraintViolations.size());
        assertEquals(EMPTY_ERROR_MESSAGE, constraintViolations.iterator().next().getMessage());
    }

    public static User getUser() {
        User user = new User();
        user.setUsername("myName");
        user.setPassword("myPassword");
        user.setDisplayName("myDisplayName");
        user.setDisplayRole("myDisplayRole");

        return user;
    }
}
