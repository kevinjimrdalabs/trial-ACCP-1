package com.rdalabs.automation.testing.report;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.rdalabs.automation.testing.annotation.Description;
import com.rdalabs.automation.testing.annotation.DisplayName;
import com.rdalabs.automation.testing.exp.ReportingAutomationException;
import com.rdalabs.automation.testing.report.model.XmlTestCaseModel;
import com.rdalabs.automation.testing.report.model.XmlTestCaseModel.XmlTestCaseStatus;
import com.rdalabs.automation.testing.report.model.XmlTestCaseModelId;
import com.rdalabs.automation.testing.report.model.XmlTestSuiteModel;
import com.rdalabs.automation.testing.report.model.XmlTestSuiteTestClassModel;
import com.rdalabs.automation.testing.report.model.XmlTestSuiteTestClassModelId;

public class TestmoXmlReporterGeneratorProcessor {

  private static final Logger LOG = LoggerFactory
      .getLogger(TestmoXmlReporterGeneratorProcessor.class);

  private TestmoXmlReporterGenerator generator;

  private DecimalFormat decfor = new DecimalFormat("0.000");

  public TestmoXmlReporterGeneratorProcessor(TestmoXmlReporterGenerator generator) {
    this.generator = generator;
  }

  public void process() {

    processSuiteXml();

    processTestMethodAnnotation();

    processTestMethodResult();

    processAggregateResult();

  }

  private void processAggregateResult() {

    LOG.debug("Process Aggregate Result -----------------------");

    for (XmlTestSuiteModel suite : this.generator.getTestRun().getTestSuites().values()) {

      LOG.info("Sute : {}", suite.getName());

      childSuiteAggregateResult(suite);

    }

  }

  private void childSuiteAggregateResult(XmlTestSuiteModel parent) {

    for (XmlTestSuiteModel suite : parent.getTestSuites().values()) {

      LOG.debug("SUT ----- {}", suite.getName());

      childSuiteAggregateResult(suite);

    }

    for (XmlTestSuiteTestClassModel suiteTestClass : parent.getSuiteTestClasses().values()) {

      LOG.debug("STC ----- {}", suiteTestClass.getName());

      long startMillis = 0;
      long endMillis = 0;

      int totalPassed = 0;
      int totalFailed = 0;
      int totalError = 0;
      int totalSkipped = 0;

      for (XmlTestCaseModel testCase : suiteTestClass.getTestCases().values()) {

        LOG.trace("STM ----- N:{} S:{} ST:{} ET: {}", testCase.getName(), testCase.getStatus(),
            testCase.getStartMillis(), testCase.getEndMillis());

        if (startMillis >= 0) {
          startMillis = testCase.getStartMillis();
        }

        if (endMillis >= 0) {
          endMillis = testCase.getEndMillis();
        }

        if (testCase.getStatus() == XmlTestCaseStatus.PASSED) {
          totalPassed++;
        } else if (testCase.getStatus() == XmlTestCaseStatus.FAILED) {
          totalFailed++;
        } else if (testCase.getStatus() == XmlTestCaseStatus.SKIPPED) {
          totalError++;
        } else if (testCase.getStatus() == XmlTestCaseStatus.ERROR) {
          totalSkipped++;
        } else {
          throw new ReportingAutomationException(
              String.format("%s status not support!", testCase.getStatus()));
        }
      }
    }

  }

  private void processTestMethodResult() {

    LOG.debug("Process Test Method Result -----------------------");

    this.generator.getSuites().forEach(suite -> {

      LOG.debug("Suite : {}", suite.getName());

      suite.getResults().forEach((suiteResultKey, suiteResult) -> {

        XmlTestCaseModel testCaseModel;

        LOG.debug("Passed Tests : ==============");

        Set<ITestResult> passed = suiteResult.getTestContext().getPassedTests().getAllResults();

        for (ITestResult testResult : passed) {

          testCaseModel = setTestResultProperty(testResult);

        }

        LOG.debug("Failed Tests : ==============");

        int failed = 0;
        int error = 0;

        for (ITestResult testResult : suiteResult.getTestContext().getFailedTests()
            .getAllResults()) {

          testCaseModel = setTestResultProperty(testResult);

          if (testCaseModel.getStatus() == XmlTestCaseStatus.ERROR) {

            failed++;

          } else {

            error++;

          }

        }

        LOG.debug("Skip Tests : ==============");

        Set<ITestResult> skipped = suiteResult.getTestContext().getSkippedTests().getAllResults();

        for (ITestResult testResult : skipped) {

          testCaseModel = setTestResultProperty(testResult);

        }

        LOG.debug("Total Passed : {}, Total Failed : {}, Total Error : {}, Total Skipped : {}",
            passed.size(), failed, error, skipped.size());

      });

    });
  }

  private XmlTestCaseModel setTestResultProperty(ITestResult testResult) {

    ITestNGMethod method = testResult.getMethod();

    XmlTestSuiteTestClassModelId testModelId = XmlTestSuiteTestClassModelId.builder()
        .xmlSuiteName(method.getXmlTest().getSuite().getName())
        .xmlTestName(method.getXmlTest().getName()).className(method.getRealClass().getName())
        .build();

    XmlTestCaseModelId testCaseModelId = XmlTestCaseModelId.builder().classId(testModelId)
        .qualifiedName(method.getQualifiedName()).build();

    LOG.debug("Name : {}", testResult.getName());
    LOG.debug("Test Case Id : {}", testCaseModelId);
    LOG.debug("Status : {}", testResult.getStatus());

    if (!this.generator.getTestRun().isContain(testModelId)) {

      throw new ReportingAutomationException(
          String.format("Test method not found %s", testResult.getMethod().getQualifiedName()));

    }

    XmlTestCaseModel testCaseModel = this.generator.getTestRun().get(testCaseModelId);

    float time = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;

    testCaseModel.setClassname(testCaseModel.getParent().getId().getClassName());
    testCaseModel.setStartMillis(testResult.getStartMillis());
    testCaseModel.setEndMillis(testResult.getEndMillis());
    testCaseModel.setTime(time);

    String type = null;

    if (testResult.getStatus() == 1) {

      testCaseModel.setStatus(XmlTestCaseStatus.PASSED);

    } else if (testResult.getStatus() == 2 || testResult.getStatus() == 3) {

      testCaseModel.setMessage(testResult.getThrowable().getMessage());
      testCaseModel.setStackTrace(ExceptionUtils.getMessage(testResult.getThrowable()));
      testCaseModel.setStatus(XmlTestCaseStatus.FAILED);

      LOG.debug("Message : {}", testCaseModel.getMessage());
      LOG.debug("Stacktrace : {}", testCaseModel.getStackTrace());

      if (testCaseModel.getStackTrace().contains(":")) {

        type = testCaseModel.getStackTrace().split(":")[0];

        if (testResult.getStatus() == 3) {

          testCaseModel.setStatus(XmlTestCaseStatus.SKIPPED);

        } else if ("AssertionError".compareTo(type) == 0) {

          testCaseModel.setStatus(XmlTestCaseStatus.FAILED);

        } else {

          testCaseModel.setStatus(XmlTestCaseStatus.ERROR);

        }

      } else {

        type = "N/C";

        testCaseModel.setStatus(XmlTestCaseStatus.ERROR);

      }

      LOG.debug("Type : {}", type);

      testCaseModel.setType(type);

    } else {

      LOG.warn("{} status not support!");

      throw new ReportingAutomationException(
          String.format("%s status not support!", testResult.getStatus()));

    }

    LOG.debug("Time : {}", this.decfor.format(time));
    LOG.debug("Status : {}", testCaseModel.getStatus());

    return testCaseModel;
  }

  private void processTestMethodAnnotation() {

    LOG.debug("Process Test Method Annotation -----------------------");

    this.generator.getSuites().forEach(suite -> {

      LOG.debug("Suite : {}", suite.getName());

      suite.getAllMethods().forEach(testMethod -> {

        Class testClass = testMethod.getInstance().getClass();

        XmlTestSuiteTestClassModelId testModelId = XmlTestSuiteTestClassModelId.builder()
            .xmlSuiteName(testMethod.getXmlTest().getSuite().getName())
            .xmlTestName(testMethod.getXmlTest().getName()).className(testClass.getName()).build();

        LOG.debug("Test Method -----------------------");
        LOG.debug("Test Suite Class Id : {}", testModelId);
        LOG.debug("Class Name : {}", testClass.getName());
        LOG.debug("Method Name : {}", testMethod.getMethodName());
        LOG.debug("Qualified Name : {}", testMethod.getQualifiedName());
        LOG.debug("Id : {}", testMethod.getId());

        boolean foundTestClass = this.generator.getTestRun().isContain(testModelId);

        XmlTestSuiteTestClassModel testModel = this.generator.getTestRun().get(testModelId);

        LOG.debug("Name : {}", testModel.getName());
        LOG.debug("Class Name Found : {}", foundTestClass);
        LOG.debug("Class Annotation Updated : {}", testModel.isClassAnnotationUpdated());

        if (!foundTestClass) {
          throw new ReportingAutomationException(
              String.format("%s not found!", testClass.getName()));
        }

        if (!testModel.isClassAnnotationUpdated()) {

          LOG.debug("Class Level Annotation -----------------------");

          String name = String.format("%s.%s", testModel.getParent().getName(),
              getDisplayName(testClass).orElse(testClass.getSimpleName()));

          testModel.setName(name);

          testModel.getProperty().setDescription(getDescription(testClass).orElse(""));

          testModel.setClassAnnotationUpdated(true);

        }

        XmlTestCaseModel testCaseModel = new XmlTestCaseModel();

        XmlTestCaseModelId testCaseModelId = XmlTestCaseModelId.builder().classId(testModelId)
            .qualifiedName(testMethod.getQualifiedName()).build();

        LOG.debug("Method Level Annotation -----------------------");

        Method method = testMethod.getConstructorOrMethod().getMethod();

        testCaseModel.setName(getDisplayName(method).orElse(testMethod.getMethodName()));
        testCaseModel.setParent(testModel);
        testCaseModel.setId(testCaseModelId);

        testCaseModel.getProperty().setDescription(getDescription(method).orElse(""));

        testModel.add(testCaseModel);

        this.generator.getTestRun().add(testCaseModel);

      });

    });
  }

  private void processSuiteXml() {

    LOG.debug("Process XML Suite -----------------------");

    this.generator.getXmlSuites().forEach(suite -> {

      XmlTestSuiteModel suiteModel = new XmlTestSuiteModel();

      LOG.debug("Xml Suite : {}", suite.getName());

      suiteModel.setName(suite.getName());
      suiteModel.setSuiteName(suite.getName());

      processXmlSuite(suite, suiteModel);

      this.generator.getTestRun().add(suiteModel);

    });

  }

  private void processXmlSuite(XmlSuite parentSuite, XmlTestSuiteModel parentSuiteModel) {

    parentSuite.getTests().forEach(xmlTest -> {

      XmlTestSuiteModel suiteModel = new XmlTestSuiteModel();

      suiteModel.setName(String.format("%s.%s", parentSuiteModel.getName(), xmlTest.getName()));

      LOG.debug("Xml Test Name : {}", xmlTest.getName());

      xmlTest.getClasses().forEach(xmlClass -> {

        XmlTestSuiteTestClassModel testModel = new XmlTestSuiteTestClassModel();

        testModel.setTestName(xmlTest.getName());
        testModel.setParent(suiteModel);
        testModel.setId(XmlTestSuiteTestClassModelId.builder().xmlSuiteName(parentSuite.getName())
            .xmlTestName(xmlTest.getName()).className(xmlClass.getName()).build());

        LOG.debug("Xml Classs Id : {}", testModel.getId());

        suiteModel.add(testModel);

        this.generator.getTestRun().add(testModel);
      });

      parentSuiteModel.add(suiteModel);
    });

    parentSuite.getChildSuites().forEach(suite -> {

      LOG.debug("Child Xml Suite : {}", suite.getName());

      XmlTestSuiteModel suiteModel = new XmlTestSuiteModel();

      suiteModel.setName(String.format("%s.%s", parentSuiteModel.getName(), suite.getName()));
      suiteModel.setSuiteName(suite.getName());

      suiteModel.setParent(parentSuiteModel);

      parentSuiteModel.add(suiteModel);

      processXmlSuite(suite, suiteModel);

    });

  }

  private Optional<String> getDescription(Method method) {

    if (method.isAnnotationPresent(Description.class)) {

      Description annotation = method.getAnnotation(Description.class);

      LOG.debug("Annotation Description : {} ", annotation.value());

      return Optional.of(annotation.value());

    }

    return Optional.empty();

  }

  private Optional<String> getDisplayName(Method method) {

    if (method.isAnnotationPresent(DisplayName.class)) {

      DisplayName annotation = method.getAnnotation(DisplayName.class);

      LOG.debug("Annotation DisplayName : {} ", annotation.value());

      return Optional.of(annotation.value());

    }

    return Optional.empty();

  }

  private Optional<String> getDescription(Class clazz) {

    if (clazz.isAnnotationPresent(Description.class)) {

      Description annotation = (Description) clazz.getAnnotation(Description.class);

      LOG.debug("Annotation Description : {} ", annotation.value());

      return Optional.of(annotation.value());

    }

    return Optional.empty();

  }

  private Optional<String> getDisplayName(Class clazz) {

    if (clazz.isAnnotationPresent(DisplayName.class)) {

      DisplayName annotation = (DisplayName) clazz.getAnnotation(DisplayName.class);

      LOG.debug("Annotation DisplayName : {} ", annotation.value());

      return Optional.of(annotation.value());

    }

    return Optional.empty();

  }

}
