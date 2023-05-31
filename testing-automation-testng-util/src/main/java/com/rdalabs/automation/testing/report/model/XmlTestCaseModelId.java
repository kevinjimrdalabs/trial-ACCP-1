package com.rdalabs.automation.testing.report.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class XmlTestCaseModelId {

  private XmlTestSuiteTestClassModelId classId;

  private String qualifiedName;

  @Override
  public String toString() {
    return String.format("%s-%s-%s", this.classId.getXmlSuiteName(), this.classId.getXmlTestName(),
        this.qualifiedName);
  }
}
