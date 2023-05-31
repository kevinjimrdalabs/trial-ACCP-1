package com.rdalabs.automation.testing.report.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class XmlTestSuiteTestClassModelId {

  private String xmlSuiteName;

  private String xmlTestName;

  private String className;

  @Override
  public String toString() {
    return String.format("%s-%s-%s", this.xmlSuiteName, this.xmlTestName, this.className);
  }
}
