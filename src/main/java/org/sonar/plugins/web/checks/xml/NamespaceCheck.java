/*
 * Copyright (C) 2010 Matthijs Galesloot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonar.plugins.web.checks.xml;

import org.apache.commons.lang.StringUtils;
import org.sonar.check.Check;
import org.sonar.check.CheckProperty;
import org.sonar.check.IsoCategory;
import org.sonar.check.Priority;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.Attribute;
import org.sonar.plugins.web.node.TagNode;

/**
 * Checker for occurrence of disallowed namespaces.
 * 
 * @author Matthijs Galesloot
 */
@Check(key = "NamespaceCheck", title = "Namespace check", description = "namespace should not be used",
    priority = Priority.MAJOR,
    isoCategory = IsoCategory.Reliability)
    public class NamespaceCheck extends AbstractPageCheck {

  @CheckProperty(key = "namespaces", title="Namespaces", description = "Namespaces")
  private String[] namespaces;
  private boolean visited;

  public String getNamespaces() {
    if (namespaces != null) {
      return StringUtils.join(namespaces, ",");
    }
    return "";
  }

  public void setNamespaces(String list) {
    namespaces = StringUtils.split(list, ",");
  }

  @Override
  public void startElement(TagNode element) {

    if (visited || namespaces == null) {
      return;
    }

    for (Attribute a : element.getAttributes()) {

      if (StringUtils.startsWithIgnoreCase(a.getName(), "xmlns")) {
        for (String namespace : namespaces) {
          if (a.getValue().equalsIgnoreCase(namespace)) {
            createViolation(element);
          }
        }
      }
    }
  }
}