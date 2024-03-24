package io.sandpipers.cdk.example.apprunner;

import io.sadpipers.cdk.type.Path;
import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.AbstractEnvironment;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.apprunner.AbstractAppRunnerService.AppRunnerServiceProps;
import io.sandpipers.cdk.core.construct.apprunner.PrivateEgressEnabledAppRunnerService.PrivateEgressEnabledAppRunnerServiceProps;
import io.sandpipers.cdk.core.construct.apprunner.PublicAppRunnerService;
import io.sandpipers.cdk.core.construct.apprunner.PublicAppRunnerService.PublicAppRunnerServiceProps;
import io.sandpipers.cdk.core.construct.ec2.ExistingVpc;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apprunner.alpha.Service;
import software.amazon.awscdk.services.ec2.IVpc;

public class PublicAppRunnerServiceStack extends BaseStack {

  public PublicAppRunnerServiceStack(@NotNull final AbstractApp app,
      @NotNull final AbstractEnvironment environment) {
    super(app, environment);

    final IVpc vpc = retrieveVpc(environment.getEnvironmentName());
    final PublicAppRunnerServiceProps appRunnerServiceProps = PublicAppRunnerServiceProps.builder()
        .vpc(vpc)
        .ecrRepositoryName("sandpipers/%s".formatted(app.getApplicationName()))
        .healthCheckPath(Path.of("/actuator/health"))
        .port(8080)
        .healthCheckInterval(Duration.seconds(20))
        .healthCheckTimeout(Duration.seconds(5))
        .build();

    final Service service = new PublicAppRunnerService<>(this, SafeString.of(app.getApplicationName()), appRunnerServiceProps)
        .getService();
  }

  private IVpc retrieveVpc(final SafeString environment) {

    final String vpcName = "%s-vpc".formatted(environment.getValue());

    final ExistingVpc.VpcProps vpcProps = ExistingVpc.VpcProps.builder()
        .vpcName(vpcName)
        .build();

    return new ExistingVpc(this, SafeString.of(vpcName), vpcProps).getVpc();
  }
}