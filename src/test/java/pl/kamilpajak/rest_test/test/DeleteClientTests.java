package pl.kamilpajak.rest_test.test;

import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kamilpajak.rest_test.model.request.ClientRequest;
import pl.kamilpajak.rest_test.model.response.ClientDetails;
import pl.kamilpajak.rest_test.model.response.Message;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@SpringBootTest
@Story("Deleting clients.")
class DeleteClientTests extends BaseTest {

    @Test
    @DisplayName("Verify client is correctly deleted.")
    void verifyClientIsCorrectlyDeleted() {
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

        // When
        var deleteClientResponse = crudSteps.deleteClient(initialClient.getId());
        var returnedClientResponse = crudSteps.getClient(initialClient.getId());

        // Then
        assertThat(deleteClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        assertThat(deleteClientResponse.as(Message.class).getMessage())
                .isEqualTo("client deleted");
        assertThat(returnedClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(returnedClientResponse.as(Message.class).getMessage())
                .isEqualTo("client not found");
    }

    @Test
    @DisplayName("Verify NOT_FOUND status is returned for non-existent client.")
    void verifyNotFoundStatusIsReturnedForNonExistentClient() {
        // Given
        var nonExistentClientId = randomAlphanumeric(12);

        // When
        var returnedClientResponse = crudSteps.deleteClient(nonExistentClientId);

        // Then
        assertThat(returnedClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(returnedClientResponse.as(Message.class).getMessage())
                .isEqualTo("client not found");
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

        // When
        var deleteClientResponse = crudSteps.deleteClientUnauthorized(initialClient.getId());
        var returnedClientResponse = crudSteps.getClient(initialClient.getId());

        // Then
        assertThat(deleteClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_FORBIDDEN);
        assertThat(deleteClientResponse.as(Message.class).getMessage())
                .isEqualTo("invalid or missing api key");
        assertThat(returnedClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        assertThat(returnedClientResponse.as(ClientDetails.class))
                .isEqualTo(initialClient);
    }
}
