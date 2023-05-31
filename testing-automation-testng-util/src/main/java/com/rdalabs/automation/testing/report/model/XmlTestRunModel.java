package com.rdalabs.automation.testing.report.model;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

public class XmlTestRunModel {

  @Getter
  @Setter
  private String name;

  @Getter
  private Map<String, XmlTestSuiteModel> testSuites = new TreeMap<>();

  private Map<String, XmlTestSuiteTestClassModel> suiteTestClasses = new TreeMap<>();

  private Map<String, XmlTestCaseModel> testCases = new TreeMap<>();

  public void add(XmlTestSuiteTestClassModel testCase) {
    this.suiteTestClasses.put(testCase.getId().toString(), testCase);
  }

  public XmlTestSuiteTestClassModel get(XmlTestSuiteTestClassModelId id) {
    return this.suiteTestClasses.get(id.toString());
  }

  public boolean isContain(XmlTestSuiteTestClassModelId id) {
    return this.suiteTestClasses.containsKey(id.toString());
  }

  public void add(XmlTestSuiteModel testSuite) {
    this.testSuites.put(testSuite.getName(), testSuite);
  }

  public void add(XmlTestCaseModel testSuite) {
    this.testCases.put(testSuite.getId().toString(), testSuite);
  }

  public XmlTestCaseModel get(XmlTestCaseModelId id) {
    return this.testCases.get(id.toString());
  }

  public boolean isContain(XmlTestCaseModelId id) {
    return this.testCases.containsKey(id.toString());
  }

}
