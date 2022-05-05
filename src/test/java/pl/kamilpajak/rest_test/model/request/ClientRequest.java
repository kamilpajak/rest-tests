package pl.kamilpajak.rest_test.model.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClientRequest {

    String firstName;

    String lastName;

    String phone;
}
