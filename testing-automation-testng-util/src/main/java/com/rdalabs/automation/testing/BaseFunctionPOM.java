package com.rdalabs.automation.testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.rdalabs.automation.testing.module.service.UITestingContext;

public abstract class BaseFunctionPOM {

  protected WebDriver driver;

  private UITestingContext context;

  public BaseFunctionPOM(BaseFunctionPOM base) {
    this(base.getContext(), base.getDriver());
  }

  public BaseFunctionPOM(UITestingContext context, WebDriver driver) {

    this.context = context;
    this.driver = driver;

    PageFactory.initElements(driver, this);

  }

  public UITestingContext getContext() {
    return context;
  }

  public WebDriver getDriver() {
    return driver;
  }
}
