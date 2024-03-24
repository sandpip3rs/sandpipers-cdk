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

package io.sandpipers.cdk.core.construct.apprunner;

import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.construct.apprunner.PrivateEgressEnabledAppRunnerService.PrivateEgressEnabledAppRunnerServiceProps;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apprunner.alpha.EcrSource;
import software.amazon.awscdk.services.apprunner.alpha.HealthCheck;
import software.amazon.awscdk.services.apprunner.alpha.Service;
import software.amazon.awscdk.services.apprunner.alpha.VpcConnector;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

public class PrivateEgressEnabledAppRunnerService<T extends PrivateEgressEnabledAppRunnerServiceProps> extends
    AbstractAppRunnerService<T> {

  public PrivateEgressEnabledAppRunnerService(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id);

    final String serviceId = id.getValue();

    final IRepository repository = getRepository(scope, props);

    final EcrSource ecrSource = createEcrSource(props, repository);

    final HealthCheck healthCheck = createHealthCheck(props);

    final VpcConnector vpcConnector = createVpcConnector(scope, props);

    final Role instanceRole = createRole(serviceId, INSTANCE_ROLE_ID_SUFFIX,
        INSTANCE_ROLE_PRINCIPAL);
    props.policyStatements.forEach(instanceRole::addToPolicy);

    final Role accessRole = createRole(serviceId, ACCESS_ROLE_ID_SUFFIX, ACCESS_ROLE_PRINCIPAL);
    repository.grantPull(accessRole);

    this.service = Service.Builder.create(scope, serviceId)
        .source(ecrSource)
        .healthCheck(healthCheck)
        .vpcConnector(vpcConnector)
        .instanceRole(instanceRole)
        .accessRole(accessRole)
        .memory(props.memory)
        .cpu(props.cpu)
        .build();
  }

  @Getter
  @SuperBuilder
  public static class PrivateEgressEnabledAppRunnerServiceProps extends AppRunnerServiceProps {

    private final SubnetType subnetType = SubnetType.PRIVATE_WITH_EGRESS;
  }
}