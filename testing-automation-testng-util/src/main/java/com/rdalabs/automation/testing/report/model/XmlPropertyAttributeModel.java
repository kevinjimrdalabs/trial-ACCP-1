package com.rdalabs.automation.testing.report.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class XmlPropertyAttributeModel {

  private String name;

  private String value;

  private String body;
}
