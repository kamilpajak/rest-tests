package pl.kamilpajak.rest_test;

import org.apache.http.HttpStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kamilpajak.rest_test.model.request.ClientRequest;

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

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
            String description
    ) {
        var response = crudSteps.addNewClient(clientRequest);
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.SC_OK);
    }

    @ParameterizedTest(name = "Verify new client is NOT created - {1}.")
    @MethodSource("invalidCustomerRequests")
    void verifyNewClientIsNotCreated(
            ClientRequest clientRequest,
            String description
    ) {
        var response = crudSteps.addNewClient(clientRequest);
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }
}
