package com.codeborne.selenide.impl;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.WebDriverRunner.cleanupWebDriverExceptionMessage;

public class Describe {
  private WebElement element;
  private StringBuilder sb = new StringBuilder();

  Describe(WebElement element) {
    this.element = element;
    sb.append('<').append(element.getTagName());
  }

  Describe attr(String attributeName) {
    String attributeValue = element.getAttribute(attributeName);
    if (attributeValue != null && attributeValue.length() > 0) {
      sb.append(' ').append(attributeName).append('=').append(attributeValue);
    }
    return this;
  }

  Describe is(String name, boolean value, boolean valueToShow) {
    if (value == valueToShow) {
      sb.append(' ').append(name).append(':').append(value);
    }
    return this;
  }

  @Override
  public String toString() {
    sb.append('>').append(element.getText()).append("</").append(element.getTagName()).append('>');
    return sb.toString();
  }

  public static String describe(WebElement element) {
    try {
      if (element == null) {
        return "null";
      }
      return new Describe(element)
          .attr("id").attr("name").attr("class").attr("value").attr("disabled").attr("checked")
          .attr("type").attr("placeholder")
          .attr("onclick").attr("onClick").attr("onchange").attr("onChange")
          .is("selected", isSelected(element), true)
          .is("displayed", element.isDisplayed(), false)
          .is("enabled", element.isEnabled(), false)
          .toString();
    } catch (WebDriverException elementDoesNotExist) {
      return cleanupWebDriverExceptionMessage(elementDoesNotExist);
    }
  }

  private static boolean isSelected(WebElement element) {
    try {
      return element.isSelected();
    } catch (UnsupportedOperationException e) {
      return false;
    }
  }
}