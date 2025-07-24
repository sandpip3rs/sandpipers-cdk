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

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

/**
 * Fluent assertions for <code>AWS::IAM::Policy</code>. This should be used if the resource map is
 * extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsPolicy(String)}.
 */
@SuppressWarnings("unchecked")
public class PolicyAssert extends
    AbstractCDKResourcesAssert<PolicyAssert, Map<String, Object>> {

  private PolicyAssert(final Map<String, Object> actual) {
    super(actual, PolicyAssert.class);
  }

  public static PolicyAssert assertThat(final Map<String, Object> actual) {
    return new PolicyAssert(actual);
  }

  public PolicyAssert hasName(final String expected) {

    final String policyName = ((Map<String, String>) actual.get("Properties")).get("PolicyName");

    Assertions.assertThat(policyName)
        .isNotNull()
        .isInstanceOf(String.class)
        .matches(actualPolicyName -> actualPolicyName.matches(expected));

    return this;
  }

  public PolicyAssert hasPolicyDocumentStatement(
      @NotNull final String effect,
      @NotNull final Map<String, String> principal,
      @NotNull final List<String> resources,
      @NotNull final List<String> actions,
      @NotNull final String policyDocumentVersion) {
    final Map<String, Object> properties =
        ((Map<String, Object>) actual.get("Properties"));

    final Map<String, Object> policyDocument =
        ((Map<String, Object>) properties.get("PolicyDocument"));

    Assertions.assertThat(policyDocument)
        .hasFieldOrPropertyWithValue("Version", policyDocumentVersion);

    return hasPolicyStatement(effect, principal, resources, actions, policyDocument);
  }

  public PolicyAssert hasPolicyDocumentStatement(
      @NotNull final String effect,
      @NotNull final String policyDocumentVersion,
      @NotNull final List<String> resources,
      @NotNull final List<String> action) {
    final Map<String, Object> properties =
        ((Map<String, Object>) actual.get("Properties"));

    final Map<String, Object> policyDocument =
        ((Map<String, Object>) properties.get("PolicyDocument"));

    Assertions.assertThat(policyDocument)
        .hasFieldOrPropertyWithValue("Version", policyDocumentVersion);

    return hasPolicyStatement(effect, resources, action, policyDocument);
  }

  public PolicyAssert isAssociatedWithRole(final String expected) {
    final List<Object> roles = ((Map<String, List<Object>>) actual.get("Properties")).get("Roles");

    Assertions.assertThat(roles)
        .isNotNull()
        .isNotEmpty()
        .allMatch(role -> ((Map<String, String>) role).get("Ref").matches(expected));

    return this;
  }
}
