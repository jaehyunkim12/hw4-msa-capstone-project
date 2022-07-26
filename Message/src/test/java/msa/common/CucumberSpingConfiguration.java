package msa.common;

import io.cucumber.spring.CucumberContextConfiguration;
import msa.MessageApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { MessageApplication.class })
public class CucumberSpingConfiguration {}
