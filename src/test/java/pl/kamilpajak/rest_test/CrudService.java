package pl.kamilpajak.rest_test;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.kamilpajak.rest_test.model.request.ClientRequest;

@Component
public class CrudService {

    private static final String accessToken = RestAssured.given()
            .auth()
            .preemptive()
            .basic("egg", "f00BarbAz!")
            .when()
            .post("https://qa-interview-api.migo.money/token")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("key");

    static RequestSpecification baseSpecification() {
        return RestAssured.given()
                .filters(
//                        new AllureRestAssured(),
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter())
                .header("X-API-KEY", accessToken)
                .baseUri("https://qa-interview-api.migo.money")
                .contentType(ContentType.JSON);
    }

    public Response getAllClients() {
        return baseSpecification()
                .get("/clients")
                .andReturn();
    }

    public Response addNewClient(ClientRequest clientRequest) {
        return baseSpecification()
                .body(clientRequest)
                .post("/client")
                .andReturn();
    }
}
