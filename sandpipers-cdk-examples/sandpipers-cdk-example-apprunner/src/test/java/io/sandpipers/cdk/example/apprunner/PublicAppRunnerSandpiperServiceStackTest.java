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

package io.sandpipers.cdk.example.apprunner;

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;
import static io.sandpipers.cdk.core.AbstractApp.tagStackResources;

import static io.sandpipers.cdk.example.apprunner.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Template;

public class PublicAppRunnerSandpiperServiceStackTest extends TemplateSupport {

  @BeforeAll
  static void initAll() {
    final Application app = new Application();

    final PublicAppRunnerServiceStack publicAppRunnerServiceStack =
        new PublicAppRunnerServiceStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);

    tagStackResources(publicAppRunnerServiceStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2,
        CostCentre.SANDPIPERS, app.getApplicationName());

    template = Template.fromStack(publicAppRunnerServiceStack);
  }

  @Test
  void should_have_app_runner_service() {
    assertThat(template)
        .containsAppRunnerService("^AppRunnerService([a-zA-Z0-9]{8})?$")
        .hasHealthCheckPath("/actuator/health")
        .hasHealthCheckProtocol("HTTP")
        .hasHealthCheckInterval(20)
        .hasHealthCheckTimeout(5)
        .hasHealthCheckUnhealthyThreshold(5)
        .hasCpuConfig(1)
        .hasMemoryConfig(2)
        .hasInstanceRole("^apprunnercdkexampleInstanceRole[a-zA-Z0-9]{8}$")
        .hasServiceRole("^apprunnercdkexampleServiceRole[a-zA-Z0-9]{8}$")
        .hasEgressType("DEFAULT")
        .hasPublicAccessibility(true)
        .hasImageRepositoryType("ECR")
        .hasImageIdentifier("sandpipers/apprunner-cdk-example:latest")
        .runsOnPort("8080")
        .hasTag("APPLICATION_NAME", "apprunner-cdk-example")
        .hasTag("COST_CENTRE", "sandpipers")
        .hasTag("ENVIRONMENT", TEST);
  }

  @Test
  void should_have_app_runner_service_instance_role() {
    final Map<String, String> principal = Map.of("Service", "tasks.apprunner.amazonaws.com");
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";

    assertThat(template)
        .containsRole("^apprunnercdkexampleInstanceRole[a-zA-Z0-9]{8}$")
        .hasAssumeRolePolicyDocument(policyDocumentVersion, effect, principal, List.of("sts:AssumeRole"))
        .hasTag("COST_CENTRE", "sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "apprunner-cdk-example");
  }

  @Test
  void should_have_app_runner_service_service_role() {
    final Map<String, String> principal = Map.of("Service", "build.apprunner.amazonaws.com");
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";

    assertThat(template)
        .containsRole("^apprunnercdkexampleServiceRole[a-zA-Z0-9]{8}$")
        .hasAssumeRolePolicyDocument(policyDocumentVersion, effect, principal, List.of("sts:AssumeRole"))
        .hasTag("COST_CENTRE", "sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "apprunner-cdk-example");
  }

  @Test
  void should_have_app_runner_service_service_role_default_policy() {
    final String ecrRepoResource = "arn:aws:ecr:ap-southeast-2:111111111111:repository/sandpipers/apprunner-cdk-example";

    assertThat(template)
        .containsPolicy("^apprunnercdkexampleServiceRoleDefaultPolicy[a-zA-Z0-9]{8}$")
        .isAssociatedWithRole("^apprunnercdkexampleServiceRole[a-zA-Z0-9]{8}$")
        .hasPolicyDocumentStatement("Allow", "2012-10-17", List.of("*"), List.of("ecr:GetAuthorizationToken"))
        .hasPolicyDocumentStatement("Allow", "2012-10-17", List.of(ecrRepoResource),
            List.of("ecr:BatchCheckLayerAvailability", "ecr:GetDownloadUrlForLayer", "ecr:BatchGetImage")); // TODO finish me;
  }
}
