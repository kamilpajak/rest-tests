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

    @Step("Get all clients [Unauthorized].")
    public Response getAllClientsUnauthorized() {
        return crudService.getAllClientsUnauthorized();
    }

    @Step("Get client: '{0}'.")
    public Response getClient(String clientId) {
        return crudService.getClient(clientId);
    }

    @Step("Get client: '{0}' [Unauthorized].")
    public Response getClientUnauthorized(String clientId) {
        return crudService.getClientUnauthorized(clientId);
    }

    @Step("Add new client.")
    public Response addClient(ClientRequest clientRequest) {
        return crudService.addClient(clientRequest);
    }

    @Step("Add client [Unauthorized].")
    public Response addClientUnauthorized(ClientRequest clientRequest) {
        return crudService.addClientUnauthorized(clientRequest);
    }

    @Step("Update client: '{0}'.")
    public Response updateClient(String clientId, ClientRequest clientRequest) {
        return crudService.updateClient(clientId, clientRequest);
    }

    @Step("Update client: '{0}' [Unauthorized].")
    public Response updateClientUnauthorized(String clientId, ClientRequest clientRequest) {
        return crudService.updateClientUnauthorized(clientId, clientRequest);
    }

    @Step("Delete client: '{0}'.")
    public Response deleteClient(String clientId) {
        return crudService.deleteClient(clientId);
    }

    @Step("Delete client: '{0}' [Unauthorized].")
    public Response deleteClientUnauthorized(String clientId) {
        return crudService.deleteClientUnauthorized(clientId);
    }
}
