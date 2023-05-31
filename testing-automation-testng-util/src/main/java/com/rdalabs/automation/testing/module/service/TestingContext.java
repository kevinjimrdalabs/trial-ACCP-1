package com.rdalabs.automation.testing.module.service;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface TestingContext {

  public ProcessingYamlProperty getProperty();

  public ObjectMapper getObjectMapper();

}
