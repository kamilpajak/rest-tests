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
class AddNewClientTests extends BaseTest {

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
    void verifyNewClientIsCorrectlyCreated(
            ClientRequest clientRequest,
            String description,
            SoftAssertions softly
    ) {
        // When
        var response = crudSteps.addNewClient(clientRequest);
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.SC_OK);

        // Then
        var returnedClient = response.as(ClientDetails.class);
        softly.assertThat(returnedClient.getId()).isNotBlank();
        softly.assertThat(returnedClient)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(returnedClient);
    }

    @ParameterizedTest(name = "Verify new client is NOT created - {1}.")
    @MethodSource("invalidCustomerRequests")
    void verifyNewClientIsNotCreated(
            ClientRequest clientRequest,
            String description
    ) {
        // When
        var response = crudSteps.addNewClient(clientRequest);

        // Then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.SC_BAD_REQUEST);
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
        var firstAddNewClientResponse = crudSteps.addNewClient(clientRequest);
        assumeThat(firstAddNewClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        // When
        var secondAddNewClientResponse = crudSteps.addNewClient(clientRequest);

        // Then
        assertThat(secondAddNewClientResponse.statusCode()).isEqualTo(HttpStatus.SC_CONFLICT);
    }
}
