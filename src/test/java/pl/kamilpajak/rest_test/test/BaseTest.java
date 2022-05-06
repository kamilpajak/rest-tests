package pl.kamilpajak.rest_test.test;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kamilpajak.rest_test.CrudSteps;

@ExtendWith(SoftAssertionsExtension.class)
public class BaseTest {

    @Autowired
    protected CrudSteps crudSteps;
}
