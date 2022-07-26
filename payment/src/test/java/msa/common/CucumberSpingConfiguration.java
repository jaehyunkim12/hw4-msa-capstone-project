package msa.common;

import io.cucumber.spring.CucumberContextConfiguration;
import msa.PaymentApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { PaymentApplication.class })
public class CucumberSpingConfiguration {}
