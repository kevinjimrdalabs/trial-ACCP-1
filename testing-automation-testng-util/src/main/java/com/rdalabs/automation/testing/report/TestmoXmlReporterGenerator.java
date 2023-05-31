package com.rdalabs.automation.testing.report;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import com.rdalabs.automation.testing.report.model.XmlTestRunModel;
import com.rdalabs.automation.testing.report.model.XmlTestSuiteModel;

import lombok.Getter;

public class TestmoXmlReporterGenerator {

  private static final Logger LOG = LoggerFactory.getLogger(TestmoXmlReporterGenerator.class);

  @Getter
  private List<ISuite> suites;

  @Getter
  private List<XmlSuite> xmlSuites;

  @Getter
  private XmlTestRunModel testRun;

  private TestmoXmlReporterGeneratorProcessor processor;

  public TestmoXmlReporterGenerator(List<XmlSuite> xmlSuites, List<ISuite> suites) {
    this.xmlSuites = xmlSuites;
    this.suites = suites;

    this.processor = new TestmoXmlReporterGeneratorProcessor(this);
  }

  public void process() {

    LOG.info("Processing the TestNG suites...");

    this.testRun = new XmlTestRunModel();
    this.testRun.setName("Runner Name 1");

    LOG.debug("Process XML Suite -----------------------");

    this.processor.process();

    LOG.info("Processed the TestNG suites!");
  }

  public void generateXml() {

    LOG.info("Processing Test XML file...");

    LOG.info("Run Name : {}", this.testRun.getName());

    this.testRun.getTestSuites().forEach((k, suite) -> {

      LOG.info("Sute : {}", suite.getName());

      generateXmlSuite(suite);

    });

    if (LOG.isTraceEnabled()) {

      LOG.trace("Add All Tests --------------");

      // this.testRun.getAllTestSuites().forEach((k, suite) -> {
      //
      // LOG.trace("Sute : {}", suite.getSuiteName());
      //
      // });

    }

    LOG.info("Processed Test XML file!");
  }

  private void generateXmlSuite(XmlTestSuiteModel parent) {

    parent.getTestSuites().forEach((k, suite) -> {

      LOG.info("Sute : {}", suite.getName());

      generateXmlSuite(suite);

    });

    LOG.debug("Test Classes ====================");

    parent.getSuiteTestClasses().forEach((suiteTestCaseName, suiteTestCase) -> {

      LOG.info("Test Class Id : {} - {}", suiteTestCase.getName(), suiteTestCase.getId());

      suiteTestCase.getProperty().getProperties().forEach(pro -> {

        LOG.debug("Property {} : {} - {}", pro.getName(), pro.getValue(), pro.getBody());

      });

      LOG.debug("Test Methods ====================");

      suiteTestCase.getTestCases().forEach((testCaseName, testCase) -> {

        LOG.info("Method Name : {}", testCase.getName());

        testCase.getProperty().getProperties().forEach(pro -> {

          LOG.debug("Property {} : {} - {}", pro.getName(), pro.getValue(), pro.getBody());

        });

      });

    });

  }

  public void writingXml() {

    LOG.info("Writng Test XML file...");

    LOG.info("Written Test XML file!");

  }

}
