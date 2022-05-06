package pl.kamilpajak.rest_test.test;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kamilpajak.rest_test.model.request.ClientRequest;
import pl.kamilpajak.rest_test.model.response.ClientDetails;
import pl.kamilpajak.rest_test.model.response.Message;

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@SpringBootTest
class UpdateClientTests extends BaseTest {

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

    @ParameterizedTest(name = "Verify client is correctly updated - {1}.")
    @MethodSource("validCustomerRequests")
    void verifyClientIsCorrectlyUpdated(
            ClientRequest updateClientRequest,
            String description,
            SoftAssertions softly
    ) {
        // Given
        var addClientResponse = crudSteps.addClient(
                ClientRequest.builder()
                        .firstName(randomAlphabetic(7))
                        .lastName(randomAlphabetic(7))
                        .phone(randomAlphabetic(7))
                        .build()
        );
        assumeThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        var initialClient = addClientResponse.as(ClientDetails.class);

        // When
        var response = crudSteps.updateClient(initialClient.getId(), updateClientRequest);
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.SC_OK);

        // Then
        var returnedClient = response.as(ClientDetails.class);
        softly.assertThat(returnedClient.getId()).isEqualTo(initialClient.getId());
        softly.assertThat(returnedClient)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(updateClientRequest);
    }

    @ParameterizedTest(name = "Verify client is NOT updated - {1}.")
    @MethodSource("invalidCustomerRequests")
    void verifyClientIsNotUpdated(
            ClientRequest updateClientRequest,
            String description,
            SoftAssertions softly
    ) {
        // Given
        var addClientResponse = crudSteps.addClient(
                ClientRequest.builder()
                        .firstName(randomAlphabetic(7))
                        .lastName(randomAlphabetic(7))
                        .phone(randomAlphabetic(7))
                        .build()
        );
        assumeThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        var initialClient = addClientResponse.as(ClientDetails.class);

        // When
        var response = crudSteps.updateClient(initialClient.getId(), updateClientRequest);
        var returnedCustomer = crudSteps.getClient(initialClient.getId()).as(ClientDetails.class);

        // Then
        softly.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softly.assertThat(returnedCustomer)
                .isEqualTo(initialClient);
    }

    @Test
    @DisplayName("Verify NOT_FOUND status is returned for non-existent client.")
    void verifyNotFoundStatusIsReturnedForNonExistentClient(SoftAssertions softly) {
        // Given
        var nonExistentClientId = randomAlphanumeric(12);
        var updateClientRequest = ClientRequest.builder()
                .firstName(randomAlphabetic(7))
                .lastName(randomAlphabetic(7))
                .phone(randomAlphabetic(7))
                .build();

        // When
        var returnedClientResponse = crudSteps.updateClient(nonExistentClientId, updateClientRequest);
        var getClientResponse = crudSteps.getClient(nonExistentClientId);

        // Then
        softly.assertThat(returnedClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
        softly.assertThat(returnedClientResponse.as(Message.class).getMessage())
                .isEqualTo("client not found");
        softly.assertThat(getClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
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
        var addClientResponse = crudSteps.addClient(clientRequest);
        assumeThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        var initialClient = addClientResponse.as(ClientDetails.class);
        var updateClientRequest = ClientRequest.builder()
                .firstName(randomAlphabetic(7))
                .lastName(randomAlphabetic(7))
                .phone(randomAlphabetic(7))
                .build();

        // When
        var updateClientResponse = crudSteps.updateClientUnauthorized(initialClient.getId(), updateClientRequest);
        var getClientResponse = crudSteps.getClient(initialClient.getId());

        // Then
        assertThat(updateClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_FORBIDDEN);
        assertThat(updateClientResponse.as(Message.class).getMessage())
                .isEqualTo("invalid or missing api key");
        assertThat(getClientResponse.as(ClientDetails.class))
                .isEqualTo(initialClient);
    }
}
