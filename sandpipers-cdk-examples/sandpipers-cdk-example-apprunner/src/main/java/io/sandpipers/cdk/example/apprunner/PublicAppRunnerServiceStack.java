package io.sandpipers.cdk.example.apprunner;

import io.sadpipers.cdk.type.Path;
import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.AbstractEnvironment;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.apprunner.AppRunnerService;
import io.sandpipers.cdk.core.construct.apprunner.AppRunnerService.AppRunnerServiceProps;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;

public class PublicAppRunnerServiceStack extends BaseStack {

  public PublicAppRunnerServiceStack(@NotNull final AbstractApp app,
      @NotNull final AbstractEnvironment environment) {
    super(app, environment);

    final AppRunnerServiceProps appRunnerServiceProps = AppRunnerServiceProps.builder()
        .ecrRepositoryName("sandpipers/%s".formatted(app.getApplicationName()))
        .healthCheckPath(Path.of("/actuator/health"))
        .port(8080)
        .publiclyAccessible(true)
        .healthCheckInterval(Duration.seconds(20))
        .healthCheckTimeout(Duration.seconds(5))
        .build();

    new AppRunnerService<>(this, app.getApplicationName(), appRunnerServiceProps);
  }
}