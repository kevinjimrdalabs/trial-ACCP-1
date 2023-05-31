package com.rdalabs.automation.testing.report;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class TestmoXmlReporter implements org.testng.IReporter {

  private static final Logger LOG = LoggerFactory.getLogger(TestmoXmlReporter.class);

  @Override
  public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
      String outputDirectory) {

    LOG.info("Generating report...");

    TestmoXmlReporterGenerator generator = new TestmoXmlReporterGenerator(xmlSuites, suites);

    generator.process();

    // generator.generateXml();

    // generator.writingXml();

    LOG.info("Generated report!");
  }

}
