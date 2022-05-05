package pl.kamilpajak.rest_test;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(SoftAssertionsExtension.class)
public class BaseTest {

    @Autowired
    protected CrudSteps crudSteps;
}
