package com.rdalabs.automation.testing.module.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdalabs.automation.testing.exp.AutomationException;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;

public class DefaultUITestingContext implements UITestingContext {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultUITestingContext.class);

  public enum BrowserType {

    CHROME

  }

  @Getter
  private TestingContext context;

  @Getter
  private UITestingPropertyModel property;

  @Getter
  private BrowserType browserType;

  public DefaultUITestingContext(TestingContext context) {

    LOG.info("UI Initializing Default Context");

    this.context = context;

    loadingUiProperties();

    initiateDriver();

  }

  private void loadingUiProperties() {

    try {

      this.property = this.context.getObjectMapper().convertValue(
          this.context.getProperty().getMap("testing").get("ui"), UITestingPropertyModel.class);

    } catch (NullPointerException e) {

      throw new AutomationException("UI testing property not found!");

    }

    if (LOG.isTraceEnabled()) {

      LOG.trace("UI Properties {} ", this.property);

    }

  }

  private void initiateDriver() {

    if (this.property.getBrowser().toUpperCase().compareTo("CHROME") == 0) {

      WebDriverManager.chromedriver().setup();

      this.browserType = BrowserType.CHROME;

    } else {

      throw new AutomationException(
          String.format("%s driver not supported", this.property.getBrowser()));

    }

    LOG.info("{} browser driver detected", this.browserType);
  }

  @Override
  public WebDriver newWebDriver() {

    if (this.browserType == BrowserType.CHROME) {

      return new ChromeDriver();

    } else {

      throw new AutomationException(
          String.format("%s driver not supported", this.property.getBrowser()));
    }

  }

  @Override
  public String getURL(String path) {

    return String.format("%s%s", this.property.getUrl(), path);

  }

}
