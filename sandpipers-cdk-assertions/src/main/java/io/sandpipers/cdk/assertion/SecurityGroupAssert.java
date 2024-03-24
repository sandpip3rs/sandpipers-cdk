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
 * Fluent assertions for <code>AWS::EC2::SecurityGroup</code>. This should be used if the resource map is extracted from the AWS template.
 * Otherwise, start with {@link CDKStackAssert#containsSecurityGroup(String)}.
 */
@SuppressWarnings("unchecked")
public class SecurityGroupAssert extends
    AbstractCDKResourcesAssert<SecurityGroupAssert, Map<String, Object>> {

  private SecurityGroupAssert(final Map<String, Object> actual) {
    super(actual, SecurityGroupAssert.class);
  }

  public static SecurityGroupAssert assertThat(final Map<String, Object> actual) {
    return new SecurityGroupAssert(actual);
  }

  public SecurityGroupAssert hasVpcId(final String expectedId) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String actualVpcId = properties.get("VpcId");

    Assertions.assertThat(actualVpcId)
        .isNotBlank()
        .matches(expectedId);

    return this;
  }

  public SecurityGroupAssert hasDescription(final String expectedSecurityGroupDescription) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String actualSecurityGroupReference = properties.get("GroupDescription");

    Assertions.assertThat(actualSecurityGroupReference)
        .isNotBlank()
        .matches(expectedSecurityGroupDescription);

    return this;
  }

  public SecurityGroupAssert hasEgress(final IPv4Cidr expectedCidrIp,
      final String expectedDescription,
      final String expectedIpProtocol) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final List<Map<String, String>> securityGroupEgress = (List<Map<String, String>>) properties.get("SecurityGroupEgress");

    Assertions.assertThat(securityGroupEgress)
        .isNotEmpty()
        .anySatisfy(egress -> {
          Assertions.assertThat(egress)
              .containsEntry("CidrIp", expectedCidrIp.getValue())
              .containsEntry("Description", expectedDescription)
              .containsEntry("IpProtocol", expectedIpProtocol);
        });

    return this;
  }
}
