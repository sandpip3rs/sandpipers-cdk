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
 * Fluent assertions for <code>AWS::AppRunner::Service</code>. This should be used if the resource
 * map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsAppRunnerService(String)}.
 */
@SuppressWarnings("unchecked")
public class AppRunnerAssert extends
    AbstractCDKResourcesAssert<AppRunnerAssert, Map<String, Object>> {

  private AppRunnerAssert(final Map<String, Object> actual) {
    super(actual, AppRunnerAssert.class);
  }

  public static AppRunnerAssert assertThat(final Map<String, Object> actual) {
    return new AppRunnerAssert(actual);
  }

  public AppRunnerAssert hasHealthCheckPath(final String path) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> healthCheckConfiguration = (Map<String, Object>) properties.get("HealthCheckConfiguration");

    Assertions.assertThat(healthCheckConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("Path", path);

    return this;
  }

  public AppRunnerAssert hasHealthCheckProtocol(final String protocol) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> healthCheckConfiguration = (Map<String, Object>) properties.get("HealthCheckConfiguration");

    Assertions.assertThat(healthCheckConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("Protocol", protocol);

    return this;
  }

  public AppRunnerAssert hasHealthCheckInterval(final int interval) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> healthCheckConfiguration = (Map<String, Object>) properties.get("HealthCheckConfiguration");

    Assertions.assertThat(healthCheckConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("Interval", interval);

    return this;
  }

  public AppRunnerAssert hasHealthCheckTimeout(final int timeout) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> healthCheckConfiguration = (Map<String, Object>) properties.get("HealthCheckConfiguration");

    Assertions.assertThat(healthCheckConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("Timeout", timeout);

    return this;
  }

  public AppRunnerAssert hasHealthCheckUnhealthyThreshold(final int unhealthyThreshold) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> healthCheckConfiguration = (Map<String, Object>) properties.get("HealthCheckConfiguration");

    Assertions.assertThat(healthCheckConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("UnhealthyThreshold", unhealthyThreshold);

    return this;
  }

  public AppRunnerAssert hasHealthCheckHealthyThreshold(final int unhealthyThreshold) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> healthCheckConfiguration = (Map<String, Object>) properties.get("HealthCheckConfiguration");

    Assertions.assertThat(healthCheckConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("HealthyThreshold", unhealthyThreshold);

    return this;
  }

  public AppRunnerAssert hasCpuConfig(final int cpu) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> instanceConfiguration = (Map<String, Object>) properties.get("InstanceConfiguration");

    Assertions.assertThat(instanceConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("Cpu", "%d vCPU".formatted(cpu));
    return this;
  }

  public AppRunnerAssert hasMemoryConfig(final int memory) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> instanceConfiguration = (Map<String, Object>) properties.get("InstanceConfiguration");

    Assertions.assertThat(instanceConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("Memory", "%d GB".formatted(memory));
    return this;
  }

  public AppRunnerAssert hasInstanceRole(final String roleInstanceArnReference) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> instanceConfiguration = (Map<String, Object>) properties.get(
        "InstanceConfiguration");
    final Map<String, List<String>> instanceRoleArn = (Map<String, List<String>>) instanceConfiguration.get(
        "InstanceRoleArn");
    final List<String> roleArnFun = instanceRoleArn.get("Fn::GetAtt");

    Assertions.assertThat(roleArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(roleInstanceArnReference)));
    return this;
  }

  public AppRunnerAssert hasServiceRole(final String roleInstanceArnReference) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> instanceConfiguration =
        (Map<String, Object>) properties.get("SourceConfiguration");
    final Map<String, Object> authenticationConfiguration =
        (Map<String, Object>) instanceConfiguration.get("AuthenticationConfiguration");
    final Map<String, List<String>> instanceRoleArn =
        (Map<String, List<String>>) authenticationConfiguration.get("AccessRoleArn");
    final List<String> roleArnFun = instanceRoleArn.get("Fn::GetAtt");

    Assertions.assertThat(roleArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(roleInstanceArnReference)));
    return this;
  }

  public AppRunnerAssert hasEgressType(final String expectedEgressType) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> networkConfiguration = (Map<String, Object>) properties.get(
        "NetworkConfiguration");
    final Map<String, Object> egressConfiguration = (Map<String, Object>) networkConfiguration.get(
        "EgressConfiguration");

    final String actualRegressType = (String) egressConfiguration.get("EgressType");
    Assertions.assertThat(actualRegressType).
        isEqualTo(expectedEgressType);

    return this;
  }

  public AppRunnerAssert hasPublicAccessibility(final boolean expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> networkConfiguration = (Map<String, Object>) properties.get(
        "NetworkConfiguration");
    final Map<String, Boolean> egressConfiguration = (Map<String, Boolean>) networkConfiguration.get(
        "IngressConfiguration");

    final Boolean actualRegressType = egressConfiguration.get("IsPubliclyAccessible");
    Assertions.assertThat(actualRegressType).
        isEqualTo(expected);

    return this;
  }

  public AppRunnerAssert hasVpcConnectorArn(final String expectedVPCConnectorArn) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> networkConfiguration = (Map<String, Object>) properties.get("NetworkConfiguration");
    final Map<String, Object> egressConfiguration = (Map<String, Object>) networkConfiguration.get("EgressConfiguration");

    final Map<String, List<String>> vpcConnectorArn = (Map<String, List<String>>) egressConfiguration.get("VpcConnectorArn");
    final List<String> vpcConnectorArnFun = vpcConnectorArn.get("Fn::GetAtt");

    Assertions.assertThat(vpcConnectorArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(expectedVPCConnectorArn)));

    return this;
  }

  public AppRunnerAssert hasImageRepositoryType(final String imageRepositoryType) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> sourceConfiguration = (Map<String, Object>) properties.get(
        "SourceConfiguration");
    final Map<String, Object> imageRepository = (Map<String, Object>) sourceConfiguration.get(
        "ImageRepository");

    Assertions.assertThat(imageRepository)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("ImageRepositoryType", imageRepositoryType);

    return this;
  }

  public AppRunnerAssert hasImageIdentifier(final String expectedImageIdentifier) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> sourceConfiguration =
        (Map<String, Object>) properties.get("SourceConfiguration");
    final Map<String, Object> imageRepository =
        (Map<String, Object>) sourceConfiguration.get("ImageRepository");
    final Map<String, Object> imageIdentifier =
        (Map<String, Object>) imageRepository.get("ImageIdentifier");

    final List<String> imageArn =  ((List<Object>) imageIdentifier.get("Fn::Join")).stream()
        .filter(e -> e instanceof List<?>)
        .flatMap(e -> ((List<?>) e).stream())
        .filter(e -> e instanceof String)
        .map(e -> (String) e)
        .toList();

    Assertions.assertThat(imageArn)
        .anySatisfy( s -> Assertions.assertThat(s)
            .matches(e -> e.matches("/%s".formatted(expectedImageIdentifier))));

    return this;
  }

  public AppRunnerAssert runsOnPort(final String port) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> sourceConfiguration = (Map<String, Object>) properties.get(
        "SourceConfiguration");
    final Map<String, Object> imageRepository = (Map<String, Object>) sourceConfiguration.get(
        "ImageRepository");
    final Map<String, String> imageConfiguration = (Map<String, String>) imageRepository.get(
        "ImageConfiguration");

    Assertions.assertThat(imageConfiguration)
        .isNotEmpty()
        .hasFieldOrPropertyWithValue("Port", port);

    return this;
  }
}
