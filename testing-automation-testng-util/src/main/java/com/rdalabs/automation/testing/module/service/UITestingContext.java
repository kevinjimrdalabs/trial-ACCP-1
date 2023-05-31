package com.rdalabs.automation.testing.module.service;

import org.openqa.selenium.WebDriver;

import com.rdalabs.automation.testing.module.service.DefaultUITestingContext.BrowserType;


public interface UITestingContext {

  public TestingContext getContext();

  public UITestingPropertyModel getProperty();

  public String getURL(String path);

  public BrowserType getBrowserType();

  public WebDriver newWebDriver();
}
