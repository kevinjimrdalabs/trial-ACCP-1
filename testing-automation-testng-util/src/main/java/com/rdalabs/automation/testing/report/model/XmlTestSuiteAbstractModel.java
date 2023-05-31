package com.rdalabs.automation.testing.report.model;

import lombok.Data;

@Data
public class XmlTestSuiteAbstractModel {

  private String name;

  private int tests;

  private int failures;

  private int errors;

  private int skipped;

  private int assertions;

  private float time;

  private String timestamp;

}
