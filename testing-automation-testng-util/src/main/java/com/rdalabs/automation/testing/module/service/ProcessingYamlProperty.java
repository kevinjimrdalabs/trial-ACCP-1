package com.rdalabs.automation.testing.module.service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.rdalabs.automation.testing.exp.AutomationException;

public class ProcessingYamlProperty {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessingYamlProperty.class);

  private Map<String, Object> properties;

  public void process() {

    LOG.info("Processing YAML file");

    String filename = findFileName();

    loadFile(filename);

    LOG.debug("{} YAML loaded!", filename);

    if (LOG.isTraceEnabled()) {

      LOG.trace(this.properties.toString());

    }

  }

  public String findFileName() {

    String env = System.getenv("TESTING_ENV");

    String filename;

    if (env != null) {

      LOG.debug("Found env: {}", env);

      filename = String.format("application-%s.yaml", env);

    } else {

      filename = "application.yaml";

    }

    LOG.debug("File name : {}", filename);

    return filename;
  }

  public void loadFile(String filename) {

    try {

      URL resourceUrl = ClassLoader.getSystemResource(filename);

      Yaml yaml = new Yaml();

      this.properties = yaml.load(resourceUrl.openStream());

    } catch (IOException e) {

      throw new AutomationException("application.properties not found!");

    }

  }

  public Object get(String key) {

    checkingKey(key);

    Object value = this.properties.get(key);

    LOG.trace("Property {} = {}", key, value);

    return value;

  }

  public Map<String, Object> getMap(String key) {

    checkingKey(key);

    return (Map<String, Object>) this.properties.get(key);

  }

  private void checkingKey(String key) {

    if (!this.properties.containsKey(key)) {

      throw new AutomationException(String.format("%s property not found!", key));

    }

  }

}
