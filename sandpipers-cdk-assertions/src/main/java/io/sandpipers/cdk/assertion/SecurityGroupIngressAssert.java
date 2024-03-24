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

import io.sadpipers.cdk.type.IPv4Cidr;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for <code>AWS::EC2::SecurityGroupIngress</code>. This should be used if the resource map is extracted from the AWS template.
 * Otherwise, start with {@link CDKStackAssert#containsSecurityGroupIngressRule(String)}.
 */
@SuppressWarnings("unchecked")
public class SecurityGroupIngressAssert extends
    AbstractCDKResourcesAssert<SecurityGroupIngressAssert, Map<String, Object>> {

  private SecurityGroupIngressAssert(final Map<String, Object> actual) {
    super(actual, SecurityGroupIngressAssert.class);
  }

  public static SecurityGroupIngressAssert assertThat(final Map<String, Object> actual) {
    return new SecurityGroupIngressAssert(actual);
  }

  public SecurityGroupIngressAssert hasFromPort(final Integer expected) {
    final Map<String, Integer> properties = (Map<String, Integer>) actual.get("Properties");
    final Integer actual = properties.get("FromPort");

    Assertions.assertThat(actual)
        .isNotNull()
        .isEqualTo(expected);

    return this;
  }

  public SecurityGroupIngressAssert hasToPort(final Integer expected) {
    final Map<String, Integer> properties = (Map<String, Integer>) actual.get("Properties");
    final Integer actual = properties.get("ToPort");

    Assertions.assertThat(actual)
        .isNotNull()
        .isEqualTo(expected);

    return this;
  }

  public SecurityGroupIngressAssert hasDescription(final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String actual = properties.get("Description");

    Assertions.assertThat(actual)
        .isNotBlank()
        .matches(expected);

    return this;
  }

  public SecurityGroupIngressAssert hasIpProtocol(final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String actual = properties.get("IpProtocol");

    Assertions.assertThat(actual)
        .isNotBlank()
        .matches(expected);

    return this;
  }

  public SecurityGroupIngressAssert hasSourceSecurityGroupId(final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String actual = properties.get("SourceSecurityGroupId");

    Assertions.assertThat(actual)
        .isNotBlank()
        .matches(expected);

    return this;
  }

  public SecurityGroupIngressAssert hasGroupId(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, List<String>> groupId = (Map<String, List<String>>) properties.get("GroupId");
    final List<String> actual = groupId.get("Fn::GetAtt");

    Assertions.assertThat(actual)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .containsPattern(expected));

    return this;
  }

}
