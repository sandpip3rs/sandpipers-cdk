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

import io.sadpipers.cdk.type.Path;
import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sandpipers.cdk.core.construct.apprunner.AppRunnerService.AppRunnerServiceProps;
import java.util.List;
import java.util.Map;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Arn;
import software.amazon.awscdk.ArnComponents;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apprunner.alpha.Cpu;
import software.amazon.awscdk.services.apprunner.alpha.EcrProps;
import software.amazon.awscdk.services.apprunner.alpha.EcrSource;
import software.amazon.awscdk.services.apprunner.alpha.HealthCheck;
import software.amazon.awscdk.services.apprunner.alpha.HttpHealthCheckOptions;
import software.amazon.awscdk.services.apprunner.alpha.IVpcConnector;
import software.amazon.awscdk.services.apprunner.alpha.ImageConfiguration;
import software.amazon.awscdk.services.apprunner.alpha.Memory;
import software.amazon.awscdk.services.apprunner.alpha.Secret;
import software.amazon.awscdk.services.apprunner.alpha.Service;
import software.amazon.awscdk.services.apprunner.alpha.Source;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

@Getter
public class AppRunnerService<T extends AppRunnerServiceProps> extends Construct implements BaseConstruct {

  protected static final String ACCESS_ROLE_PRINCIPAL = "build.apprunner.amazonaws.com";
  protected static final String ACCESS_ROLE_ID = "ServiceRole";
  protected static final String INSTANCE_ROLE_PRINCIPAL = "tasks.apprunner.amazonaws.com";
  protected static final String INSTANCE_ROLE_ID = "InstanceRole";
  protected static final String PARTITION_AWS = "aws";

  protected Service service;

  public AppRunnerService(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id.getValue());

    final IRepository repository = getRepository(scope, props);

    final EcrSource ecrSource = createEcrSource(props, repository);

    final HealthCheck healthCheck = createHealthCheck(props);

    final Role instanceRole = createRole(INSTANCE_ROLE_ID, INSTANCE_ROLE_PRINCIPAL);
    props.policyStatements.forEach(instanceRole::addToPolicy);

    final Role accessRole = createRole(ACCESS_ROLE_ID, ACCESS_ROLE_PRINCIPAL);
    repository.grantPull(accessRole);

    this.service = Service.Builder.create(scope, "AppRunnerService")
        .source(ecrSource)
        .healthCheck(healthCheck)
        .vpcConnector(props.getVpcConnector())
        .instanceRole(instanceRole)
        .accessRole(accessRole)
        .memory(props.memory)
        .cpu(props.cpu)
        .isPubliclyAccessible(props.publiclyAccessible)
        .build();
  }

  protected IRepository getRepository(@NotNull final Construct scope, @NotNull final T props) {

    final String ecrRepositoryName = props.getEcrRepositoryName();

    final ArnComponents arnComponents = ArnComponents.builder()
        .partition(PARTITION_AWS)
        .region(Stack.of(scope).getRegion())
        .account(Stack.of(scope).getAccount())
        .service("ecr")
        .resource("repository")
        .resourceName(ecrRepositoryName)
        .build();

    return Repository.fromRepositoryArn(scope, ecrRepositoryName, Arn.format(arnComponents));
  }

  protected EcrSource createEcrSource(@NotNull final T props,
      @NotNull final IRepository repository) {
    final ImageConfiguration imageConfiguration = createImageConfiguration(props);

    return Source.fromEcr(EcrProps.builder()
        .imageConfiguration(imageConfiguration)
        .repository(repository)
        .tagOrDigest("latest")
        .build());
  }

  private ImageConfiguration createImageConfiguration(final T props) {
    return ImageConfiguration.builder()
        .port(props.getPort())
        .environmentSecrets(props.getEnvironmentSecrets())
        .environmentVariables(props.getEnvironmentVariables())
        .build();
  }

  protected HealthCheck createHealthCheck(@NotNull final T props) {
    return HealthCheck.http(HttpHealthCheckOptions.builder()
        .path(props.getHealthCheckPath().getValue())
        .interval(props.getHealthCheckInterval())
        .timeout(props.getHealthCheckTimeout())
        .build());
  }

  protected Role createRole(@NotNull final String idSuffix,
      @NotNull final String assumedByPrincipal) {
    return Role.Builder.create(this, idSuffix)
        .assumedBy(new ServicePrincipal(assumedByPrincipal))
        .build();
  }

  @Getter
  @SuperBuilder
  public static class AppRunnerServiceProps {

    @NotNull
    private final String ecrRepositoryName;

    @NotNull
    private final Path healthCheckPath;

    @Singular
    private final Map<String, Secret> environmentSecrets;

    @Singular
    private final Map<String, String> environmentVariables;

    @Singular
    protected final List<PolicyStatement> policyStatements;

    @Default
    private final Integer port = 8080;

    @Default
    private final Duration healthCheckInterval = Duration.seconds(5);

    @Default
    private final Duration healthCheckTimeout = Duration.seconds(2);

    @Default
    protected final Memory memory = Memory.TWO_GB;

    @Default
    protected final Cpu cpu = Cpu.ONE_VCPU;

    @Default
    protected final Boolean publiclyAccessible = false;

    @Nullable
    protected final IVpcConnector vpcConnector;

  }
}
