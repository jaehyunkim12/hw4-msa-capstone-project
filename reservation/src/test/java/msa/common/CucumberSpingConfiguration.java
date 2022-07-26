package msa.common;

import io.cucumber.spring.CucumberContextConfiguration;
import msa.ReservationApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { ReservationApplication.class })
public class CucumberSpingConfiguration {}
