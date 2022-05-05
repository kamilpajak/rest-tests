package pl.kamilpajak.rest_test;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ListAllClientsTests extends BaseTest {

    @Test
    @DisplayName("Verify list of clients is correctly returned.")
    void verifyListOfClientsIsCorrectlyReturned() {
        var response = crudSteps.getAllClients();
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
    }
}
