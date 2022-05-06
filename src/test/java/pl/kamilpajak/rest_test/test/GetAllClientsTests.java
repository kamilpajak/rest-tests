package pl.kamilpajak.rest_test.test;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kamilpajak.rest_test.model.request.ClientRequest;
import pl.kamilpajak.rest_test.model.response.ClientDetails;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@SpringBootTest
class GetAllClientsTests extends BaseTest {

    @Test
    @DisplayName("Verify list of clients is correctly returned.")
    void verifyListOfClientsIsCorrectlyReturned() {
        // Given
        var addNewClientResponse = crudSteps.addNewClient(
                ClientRequest.builder()
                        .firstName(randomAlphabetic(7))
                        .lastName(randomAlphabetic(7))
                        .phone(randomAlphabetic(7))
                        .build());
        assumeThat(addNewClientResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);

        // When
        var getAllClientsResponse = crudSteps.getAllClients();

        // Then
        assertThat(getAllClientsResponse.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
        var returnedClients = getAllClientsResponse.jsonPath().getList("clients", ClientDetails.class);
        assertThat(returnedClients).contains(addNewClientResponse.as(ClientDetails.class));
    }
}
