package pl.kamilpajak.rest_test.model.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Message {

    String message;
}
