package com.rdalabs.automation.testing.report.model;

import java.util.Map;
import java.util.TreeMap;

import lombok.Data;

@Data
public class XmlTestSuiteTestClassModel extends XmlTestSuiteAbstractModel {

  private String testName;

  private XmlTestSuiteTestClassModelId id;

  private boolean classAnnotationUpdated = false;

  private Map<String, XmlTestCaseModel> testCases = new TreeMap<>();

  private XmlTestSuiteModel parent;

  private XmlPropertyModel property = new XmlPropertyModel();

  public void add(XmlTestCaseModel testCase) {
    this.testCases.put(testCase.getId().toString(), testCase);
  }

}
