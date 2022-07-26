package msa.common;

import io.cucumber.spring.CucumberContextConfiguration;
import msa.ViewpageApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { ViewpageApplication.class })
public class CucumberSpingConfiguration {}
