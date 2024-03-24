package io.sandpipers.cdk.example.apprunner;

import static software.amazon.awscdk.services.ec2.SubnetType.PRIVATE_ISOLATED;

import io.sadpipers.cdk.type.Path;
import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.AbstractEnvironment;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.apprunner.AppRunnerService;
import io.sandpipers.cdk.core.construct.apprunner.AppRunnerService.AppRunnerServiceProps;
import io.sandpipers.cdk.core.construct.ec2.ExistingVpc;
import io.sandpipers.cdk.core.construct.ec2.VpcConnector;
import io.sandpipers.cdk.core.construct.ec2.VpcConnector.VpcConnectorProps;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.ec2.IVpc;

public class PrivateIsolatedAppRunnerServiceStack extends BaseStack {

  public PrivateIsolatedAppRunnerServiceStack(@NotNull final AbstractApp app, @NotNull final AbstractEnvironment environment) {
    super(app, environment);

    // TODO: Add security group for VPC connector to ensure only API Gateway can access the service
    /*

    SecurityGroup securityGroup = SecurityGroup.Builder.create(scope, "VpcConnectorSecurityGroup")
        .vpc(vpc)
        .description("Security group for VPC connector")
        .build();

    // Allow inbound traffic only from the API Gateway security group
    securityGroup.addIngressRule(
        SecurityGroup.fromSecurityGroupId(scope, "ApiGatewaySecurityGroup", "sg-1234567890abcdef"),
        Port.allTraffic(),
        "Allow traffic from API Gateway"
    );

}
     */
    final VpcConnector vpcConnector = createVpcConnector(app, environment);

    final AppRunnerServiceProps appRunnerServiceProps = AppRunnerServiceProps.builder()
        .vpcConnector(vpcConnector.getVpcConnector())
        .ecrRepositoryName("sandpipers/%s".formatted(app.getApplicationName()))
        .healthCheckPath(Path.of("/actuator/health"))
        .build();

    new AppRunnerService<>(this, SafeString.of(app.getApplicationName()), appRunnerServiceProps).getService();
  }

  private @NotNull VpcConnector createVpcConnector(@NotNull final AbstractApp app,
      @NotNull final AbstractEnvironment environment) {
    final IVpc vpc = retrieveVpc(environment.getEnvironmentName());
    final VpcConnectorProps vpcConnectorProps = VpcConnectorProps.builder()
        .vpc(vpc)
        .subnetType(PRIVATE_ISOLATED)
        .build();
    return new VpcConnector(this, SafeString.of("%sVpcConnector".formatted(app.getApplicationName())), vpcConnectorProps);
  }

  private IVpc retrieveVpc(final SafeString environment) {

    final String vpcName = "%s-vpc".formatted(environment.getValue());

    final ExistingVpc.VpcProps vpcProps = ExistingVpc.VpcProps.builder()
        .vpcName(vpcName)
        .build();

    return new ExistingVpc(this, SafeString.of(vpcName), vpcProps).getVpc();
  }
}
