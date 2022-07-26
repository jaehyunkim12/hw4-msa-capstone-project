package msa.common;

import io.cucumber.spring.CucumberContextConfiguration;
import msa.RoomApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { RoomApplication.class })
public class CucumberSpingConfiguration {}
