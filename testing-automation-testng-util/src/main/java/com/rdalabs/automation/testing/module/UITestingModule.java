package com.rdalabs.automation.testing.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.rdalabs.automation.testing.module.service.DefaultUITestingContext;
import com.rdalabs.automation.testing.module.service.TestingContext;
import com.rdalabs.automation.testing.module.service.UITestingContext;

public class UITestingModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(UITestingModule.class);

  @Inject
  private TestingContext context;

  @Override
  protected void configure() {

    LOG.info("Configuring UI Testing Module!");

    DefaultUITestingContext context = new DefaultUITestingContext(this.context);

    bind(UITestingContext.class).toInstance(context);
  }
}
