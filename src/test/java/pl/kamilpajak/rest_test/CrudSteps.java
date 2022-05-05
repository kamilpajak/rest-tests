package pl.kamilpajak.rest_test;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kamilpajak.rest_test.model.request.ClientRequest;

@Component
public class CrudSteps {

    @Autowired
    private CrudService crudService;

    @Step("Get all clients.")
    public Response getAllClients() {
        return crudService.getAllClients();
    }

    @Step("Add new client.")
    public Response addNewClient(ClientRequest clientRequest) {
        return crudService.addNewClient(clientRequest);
    }
}
