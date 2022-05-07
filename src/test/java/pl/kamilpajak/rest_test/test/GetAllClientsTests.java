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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@SpringBootTest
@Story("Getting all clients.")
class GetAllClientsTests extends BaseTest {

    @Test
    @DisplayName("Verify list of clients is correctly returned.")
    void verifyListOfClientsIsCorrectlyReturned() {
        // Given
        var addClientResponse = crudSteps.addClient(
                ClientRequest.builder()
                        .firstName(randomAlphabetic(7))
                        .lastName(randomAlphabetic(7))
                        .phone(randomAlphabetic(7))
                        .build());
        assumeThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);

        // When
        var getAllClientsResponse = crudSteps.getAllClients();

        // Then
        assertThat(getAllClientsResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        var returnedClients = getAllClientsResponse.jsonPath().getList("clients", ClientDetails.class);
        assertThat(returnedClients)
                .contains(addClientResponse.as(ClientDetails.class));
    }

    @Test
    @DisplayName("Verify FORBIDDEN status is returned.")
    void verifyForbiddenStatusIsReturned() {
        // Given
        var addClientResponse = crudSteps.addClient(
                ClientRequest.builder()
                        .firstName(randomAlphabetic(7))
                        .lastName(randomAlphabetic(7))
                        .phone(randomAlphabetic(7))
                        .build());
        assumeThat(addClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);

        // When
        var getAllClientsResponse = crudSteps.getAllClientsUnauthorized();

        // Then
        assertThat(getAllClientsResponse.statusCode())
                .isEqualTo(HttpStatus.SC_FORBIDDEN);
        assertThat(getAllClientsResponse.as(Message.class).getMessage())
                .isEqualTo("invalid or missing api key");
    }
}
