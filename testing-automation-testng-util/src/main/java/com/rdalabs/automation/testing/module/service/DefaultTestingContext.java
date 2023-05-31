package com.rdalabs.automation.testing.module.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public class DefaultTestingContext implements TestingContext {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultTestingContext.class);

  @Getter
  private ProcessingYamlProperty property = new ProcessingYamlProperty();

  @Getter
  private ObjectMapper objectMapper = new ObjectMapper();

  public DefaultTestingContext() {

    LOG.info("Initializing Default Context");

    this.property.process();

  }

}
