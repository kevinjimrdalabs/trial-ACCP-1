package com.rdalabs.automation.testing.report.model;

import java.util.ArrayList;
import java.util.List;

import com.rdalabs.automation.testing.annotation.Priority;

import lombok.Getter;

@Getter
public class XmlPropertyModel {

  private List<XmlPropertyAttributeModel> properties = new ArrayList<>();

  public void add(XmlPropertyAttributeModel property) {
    this.properties.add(property);
  }

  public void setPriority(Priority priority) {
    this.properties.add(XmlPropertyAttributeModel.builder().name("priority")
        .value(priority.value().toString().toLowerCase()).build());
  }

  public void setDescription(String description) {
    this.properties.add(XmlPropertyAttributeModel.builder().name("description")
        .body(String.format("<![CDATA[%s]]>", description)).build());
  }
}
