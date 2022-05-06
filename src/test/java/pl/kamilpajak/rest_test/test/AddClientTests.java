package pl.kamilpajak.rest_test.test;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kamilpajak.rest_test.model.request.ClientRequest;
import pl.kamilpajak.rest_test.model.response.ClientDetails;

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@SpringBootTest
class AddClientTests extends BaseTest {

    private static Stream<Arguments> validCustomerRequests() {
        return Stream.of(
                Arguments.of(
                        ClientRequest.builder()
                                .firstName(randomAlphabetic(1))
                                .lastName(randomAlphabetic(1))
                                .phone(randomAlphabetic(1))
                                .build(), "min property length"),
                Arguments.of(
                        ClientRequest.builder()
                                .firstName(randomAlphabetic(50))
                                .lastName(randomAlphabetic(50))
                                .phone(randomAlphabetic(50))
                                .build(), "max property length"),

                Arguments.of(
                        ClientRequest.builder()
                                .firstName(random(7))
                                .lastName(random(7))
                                .phone(random(7))
                                .build(), "all types of characters")
        );
    }

    private static Stream<Arguments> invalidCustomerRequests() {
        return Stream.of(
                Arguments.of(
                        ClientRequest.builder()
                                .firstName(null)
                                .lastName(randomAlphabetic(7))
                                .phone(randomAlphabetic(7))
                                .build(), "first name missing"),
                Arguments.of(
                        ClientRequest.builder()
                                .firstName(randomAlphabetic(7))
                                .lastName(null)
                                .phone(randomAlphabetic(7))
                                .build(), "last name missing"),
                Arguments.of(
                        ClientRequest.builder()
                                .firstName(randomAlphabetic(7))
                                .lastName(randomAlphabetic(7))
                                .phone(null)
                                .build(), "phone missing"),
                Arguments.of(
                        ClientRequest.builder()
                                .firstName(randomAlphabetic(51))
                                .lastName(randomAlphabetic(51))
                                .phone(randomAlphabetic(51))
                                .build(), "too long properties"
                )
        );
    }

    @ParameterizedTest(name = "Verify new client is correctly created - {1}.")
    @MethodSource("validCustomerRequests")
    void verifyClientIsCorrectlyCreated(
            ClientRequest clientRequest,
            String description,
            SoftAssertions softly
    ) {
        // When
        var addClientResponse = crudSteps.addClient(clientRequest);
        assertThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);

        // Then
        var returnedClient = addClientResponse.as(ClientDetails.class);
        softly.assertThat(returnedClient.getId()).isNotBlank();
        softly.assertThat(returnedClient)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(clientRequest);
    }

    @ParameterizedTest(name = "Verify new client is NOT created - {1}.")
    @MethodSource("invalidCustomerRequests")
    void verifyClientIsNotCreated(
            ClientRequest clientRequest,
            String description
    ) {
        // When
        var addClientResponse = crudSteps.addClient(clientRequest);

        // Then
        assertThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Verify FORBIDDEN status is returned.")
    void verifyForbiddenStatusIsReturned() {
        // Given
        var clientRequest = ClientRequest.builder()
                .firstName(randomAlphabetic(7))
                .lastName(randomAlphabetic(7))
                .phone(randomAlphabetic(7))
                .build();

        // When
        var addClientResponse = crudSteps.addClientUnauthorized(clientRequest);

        // Then
        assertThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_FORBIDDEN);
    }

    @Disabled("It is possible to create two customers with same details.")
    @Test
    @DisplayName("Verify customer cannot be created if already exists.")
    void verifyCustomerCannotBeCreatedIfAlreadyExists() {
        // Given
        var clientRequest = ClientRequest.builder()
                .firstName(randomAlphabetic(7))
                .lastName(randomAlphabetic(7))
                .phone(randomAlphabetic(7))
                .build();
        var firstAddClientResponse = crudSteps.addClient(clientRequest);
        assumeThat(firstAddClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        // When
        var secondAddClientResponse = crudSteps.addClient(clientRequest);

        // Then
        assertThat(secondAddClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_CONFLICT);
    }
}
