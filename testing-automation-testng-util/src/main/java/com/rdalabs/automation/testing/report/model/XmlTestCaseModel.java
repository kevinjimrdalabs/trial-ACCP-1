package com.rdalabs.automation.testing.report.model;

import lombok.Data;

@Data
public class XmlTestCaseModel {

  public enum XmlTestCaseStatus {
    PASSED, SKIPPED, FAILED, ERROR
  }

  private String stackTrace;

  private String name;

  private String classname;

  private long startMillis;

  private long endMillis;

  private float time;

  private XmlPropertyModel property = new XmlPropertyModel();

  private XmlTestSuiteTestClassModel parent;

  private String message;

  private String type;

  private XmlTestCaseStatus status;

  private XmlTestCaseModelId id;

}
