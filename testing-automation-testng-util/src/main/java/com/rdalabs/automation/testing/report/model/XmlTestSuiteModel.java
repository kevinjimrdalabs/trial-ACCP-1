package com.rdalabs.automation.testing.report.model;

import java.util.Map;
import java.util.TreeMap;

import lombok.Data;

@Data
public class XmlTestSuiteModel extends XmlTestSuiteAbstractModel {

  private String suiteName;

  private XmlTestSuiteModel parent;

  private Map<String, XmlTestSuiteModel> testSuites = new TreeMap<>();

  private Map<String, XmlTestSuiteTestClassModel> suiteTestClasses = new TreeMap<>();

  public void add(XmlTestSuiteModel testSuite) {
    this.testSuites.put(testSuite.getName(), testSuite);
  }

  public void add(XmlTestSuiteTestClassModel testCase) {
    this.suiteTestClasses.put(testCase.getId().toString(), testCase);
  }

}
