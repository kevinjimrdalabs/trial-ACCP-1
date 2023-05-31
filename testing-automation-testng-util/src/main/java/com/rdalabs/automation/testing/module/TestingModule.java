package com.rdalabs.automation.testing.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.rdalabs.automation.testing.module.service.DefaultTestingContext;
import com.rdalabs.automation.testing.module.service.TestingContext;

public class TestingModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(TestingModule.class);

  @Override
  protected void configure() {

    LOG.info("Configuring Testing Module!");

    DefaultTestingContext context = new DefaultTestingContext();

    bind(TestingContext.class).toInstance(context);

  }

}
