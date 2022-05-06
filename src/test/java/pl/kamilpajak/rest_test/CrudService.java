package pl.kamilpajak.rest_test;

import io.qameta.allure.restassured.AllureRestAssured;
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
                        new AllureRestAssured(),
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter())
                .baseUri("https://qa-interview-api.migo.money")
                .contentType(ContentType.JSON);
    }

    public Response getAllClients() {
        return baseSpecification()
                .header("X-API-KEY", accessToken)
                .get("/clients")
                .andReturn();
    }

    public Response getAllClientsUnauthorized() {
        return baseSpecification()
                .get("/clients")
                .andReturn();
    }

    public Response getClient(String clientId) {
        return baseSpecification()
                .header("X-API-KEY", accessToken)
                .get("/client/{clientId}", clientId)
                .andReturn();
    }

    public Response getClientUnauthorized(String clientId) {
        return baseSpecification()
                .get("/client/{clientId}", clientId)
                .andReturn();
    }

    public Response addClient(ClientRequest clientRequest) {
        return baseSpecification()
                .header("X-API-KEY", accessToken)
                .body(clientRequest)
                .post("/client")
                .andReturn();
    }

    public Response addClientUnauthorized(ClientRequest clientRequest) {
        return baseSpecification()
                .body(clientRequest)
                .post("/client")
                .andReturn();
    }

    public Response updateClient(String clientId, ClientRequest clientRequest) {
        return baseSpecification()
                .header("X-API-KEY", accessToken)
                .body(clientRequest)
                .put("/client/{clientId}", clientId)
                .andReturn();
    }

    public Response updateClientUnauthorized(String clientId, ClientRequest clientRequest) {
        return baseSpecification()
                .body(clientRequest)
                .put("/client/{clientId}", clientId)
                .andReturn();
    }

    public Response deleteClient(String clientId) {
        return baseSpecification()
                .header("X-API-KEY", accessToken)
                .delete("/client/{clientId}", clientId)
                .andReturn();
    }

    public Response deleteClientUnauthorized(String clientId) {
        return baseSpecification()
                .delete("/client/{clientId}", clientId)
                .andReturn();
    }
}
