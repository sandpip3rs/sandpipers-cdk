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

/**
 * Fluent assertions for <code>AWS::AppRunner::VpcConnector</code>. This should be used if the resource map is extracted from the AWS template.
 * Otherwise, start with {@link CDKStackAssert#containsVpcConnector(String)}.
 */
@SuppressWarnings("unchecked")
public class VpcConnectorAssert extends
    AbstractCDKResourcesAssert<VpcConnectorAssert, Map<String, Object>> {

  private VpcConnectorAssert(final Map<String, Object> actual) {
    super(actual, VpcConnectorAssert.class);
  }

  public static VpcConnectorAssert assertThat(final Map<String, Object> actual) {
    return new VpcConnectorAssert(actual);
  }

  public VpcConnectorAssert hasSecurityGroup(final String expectedSecurityGroupReference) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final List<Map<String, Object>> securityGroups = (List<Map<String, Object>>) properties.get("SecurityGroups");

    Assertions.assertThat(securityGroups)
        .isNotEmpty()
        .anySatisfy(sg -> {
          final List<String> fnGetAtt = (List<String>) sg.get("Fn::GetAtt");
          Assertions.assertThat(fnGetAtt)
              .isNotEmpty()
              .anySatisfy(value -> Assertions.assertThat(value)
                  .matches(expectedSecurityGroupReference));
        });

    return this;
  }

  public VpcConnectorAssert hasSubnet(final String expectedSubnet) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final List<String> subnets = (List<String>) properties.get("Subnets");

    Assertions.assertThat(subnets)
        .isNotEmpty()
        .contains(expectedSubnet);

    return this;
  }
}
