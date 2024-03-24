/*
 *  Licensed to Muhammad Hamadto
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package io.sandpipers.cdk.assertion;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public abstract class AbstractCDKResourcesAssert<SELF extends AbstractCDKResourcesAssert<SELF, ACTUAL>, ACTUAL extends Map<String, Object>> extends
    AbstractAssert<SELF, ACTUAL> {

  protected AbstractCDKResourcesAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public SELF hasTag(final String key, final Object value) {
    hasTag("Tags", key, value);
    return myself;
  }

  public SELF hasTag(final String tagsMapId,
      final String key,
      final Object value) {

    final Map<String, Map<String, Object>> properties =
        (Map<String, Map<String, Object>>) actual.get("Properties");

    final List<Object> tags = (List<Object>) properties.get(tagsMapId);

    Assertions.assertThat(tags)
        .isNotEmpty()
        .contains(Map.of("Key", key, "Value", value));
    return myself;
  }

  public SELF hasDependency(final String expected) {

    final List<String> actualDependency = (List<String>) actual.get("DependsOn");

    Assertions.assertThat(actualDependency)
        .isInstanceOf(List.class)
        .anyMatch(s -> s.matches(expected));

    return myself;
  }

  public SELF hasUpdateReplacePolicy(final String expected) {

    final String actualUpdateReplacePolicy = (String) actual.get("UpdateReplacePolicy");

    Assertions.assertThat(actualUpdateReplacePolicy)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasDeletionPolicy(final String expected) {

    final String actualDeletionPolicy = (String) actual.get("DeletionPolicy");

    Assertions.assertThat(actualDeletionPolicy)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasEnvironmentVariable(final String key, final String value) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> environment = (Map<String, Object>) properties.get("Environment");
    final Map<String, Object> variables = (Map<String, Object>) environment.get("Variables");

    Assertions.assertThat(variables.get(key))
        .isInstanceOf(String.class)
        .isEqualTo(value);

    return myself;
  }

  public SELF hasDescription(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String description = (String) properties.get("Description");

    Assertions.assertThat(description)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasMemorySize(final Integer expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer memorySize = (Integer) properties.get("MemorySize");

    Assertions.assertThat(memorySize)
        .isInstanceOf(Integer.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasRuntime(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String runtime = (String) properties.get("Runtime");

    Assertions.assertThat(runtime)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasTimeout(final Integer timeoutInSeconds) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer timeout = (Integer) properties.get("Timeout");

    Assertions.assertThat(timeout)
        .isInstanceOf(Integer.class)
        .isEqualTo(timeoutInSeconds);

    return myself;
  }

  public SELF hasPolicyStatement(
      @NotNull final String effect,
      @NotNull final Map<String, String> principal,
      @NotNull final List<String> resource,
      @NotNull final List<String> actions,
      @NotNull final Map<String, Object> policyDocument) {

    return doCheckPolicyStatement(effect, principal, resource, actions, policyDocument);
  }

  public SELF hasPolicyStatement(
      @NotNull final String effect,
      @NotNull final Map<String, String> principal,
      @NotNull final List<String> actions,
      @NotNull final Map<String, Object> policyDocument) {

    return doCheckPolicyStatement(effect, principal, null, actions, policyDocument);
  }

  public SELF hasPolicyStatement(
      @NotNull final String effect,
      @NotNull final List<String> resources,
      @NotNull final List<String> actions,
      @NotNull final Map<String, Object> policyDocument) {

    return doCheckPolicyStatement(effect, null, resources, actions, policyDocument);
  }

  public SELF doCheckPolicyStatement(
      final String effect,
      final Map<String, String> principal,
      final List<String> resources,
      final List<String> actions,
      final Map<String, Object> policyDocument) {

    final List<Map<String, Object>> statements = (List<Map<String, Object>>) policyDocument.get("Statement");

    Assertions.assertThat(statements)
        .isNotNull()
        .isNotEmpty();

    Assertions.assertThat(statements)
        .anySatisfy(statement -> {
          Assertions.assertThat(statement)
              .isNotNull()
              .isNotEmpty()
              .containsEntry("Effect", effect)
              .containsEntry("Action", isNotEmpty(actions) && actions.size() == 1 ? actions.get(0) : actions);

          if (resources != null) {
            Assertions.assertThat(statement)
                .extracting("Resource")
                .satisfies(res -> {
                  if (res instanceof String) {
                    Assertions.assertThat(res)
                        .isEqualTo(resources.size() == 1 ? resources.get(0) : resources);
                  } else if (res instanceof Map) {
                    final Map<String, Object> resMap = (Map<String, Object>) res;
                    if (resMap.containsKey("Ref")) {
                      Assertions.assertThat(resMap.get("Ref"))
                          .asString()
                          .matches(resources.get(0));
                    } else if (resMap.containsKey("Fn::Join")) {
                      Assertions.assertThat(flattenSourceArn(resMap))
                          .matches(resources.get(0));
                    }
                  }
                });
          }

          if (principal != null) {
            Assertions.assertThat(statement)
                .extracting("Principal")
                .asInstanceOf(InstanceOfAssertFactories.MAP)
                .matches(e -> e.keySet().containsAll(principal.keySet()) && e.values().containsAll(principal.values()));
          }
        });

    return myself;
  }

  protected String flattenSourceArn(final Map<String, Object> sourceArnMap) {
    if (org.apache.commons.collections4.MapUtils.isEmpty(sourceArnMap)) {
      return "";
    }
    // Get the Fn::Join list from the SourceArn map
    final List<Object> joinList = (List<Object>) sourceArnMap.get("Fn::Join");

    // Create a StringBuilder to construct the ARN
    final StringBuilder arnBuilder = new StringBuilder();

    // Concatenate the elements of the joinList
    for (final Object element : joinList) {
      if (element instanceof String) {
        arnBuilder.append(element);
      } else if (element instanceof List) {
        // Handle nested lists
        for (final Object nestedElement : (List<Object>) element) {
          if (nestedElement instanceof String) {
            arnBuilder.append(nestedElement);
          } else if (nestedElement instanceof Map) {
            final Map<String, String> nestedMap = (Map<String, String>) nestedElement;
            final String refValue = nestedMap.get("Ref");
            if (refValue != null) {
              arnBuilder.append(refValue);
            }
          }
        }
      } else if (element instanceof Map) {
        // Handle nested maps
        final Map<String, String> nestedMap = (Map<String, String>) element;
        final String refValue = nestedMap.get("Ref");
        if (refValue != null) {
          arnBuilder.append(refValue);
        }
      }
    }

    // Get the final flattened ARN string
    return arnBuilder.toString()
        .replace("AWS::Partition", "aws")
        .replace("AWS::Region", "ap-southeast-2");
  }
}
