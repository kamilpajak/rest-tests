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

    @Step("Get client: '{0}'.")
    public Response getClient(String clientId) {
        return crudService.getClient(clientId);
    }

    @Step("Add new client.")
    public Response addNewClient(ClientRequest clientRequest) {
        return crudService.addNewClient(clientRequest);
    }

    @Step("Update client: '{0}'.")
    public Response updateClient(String clientId, ClientRequest clientRequest) {
        return crudService.updateClient(clientId, clientRequest);
    }

    @Step("Delete client: '{0}'.")
    public Response deleteClient(String clientId) {
        return crudService.deleteClient(clientId);
    }
}
