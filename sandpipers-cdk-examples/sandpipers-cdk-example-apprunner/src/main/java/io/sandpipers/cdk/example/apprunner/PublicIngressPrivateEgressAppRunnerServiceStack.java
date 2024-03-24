//package io.sandpipers.cdk.example.apprunner;
//
//import static software.amazon.awscdk.services.ec2.SubnetType.PUBLIC;
//
//import io.sadpipers.cdk.type.AWSEcrImageIdentifier;
//import io.sadpipers.cdk.type.NumericString;
//import io.sadpipers.cdk.type.Path;
//import io.sadpipers.cdk.type.SafeString;
//import io.sandpipers.cdk.core.AbstractApp;
//import io.sandpipers.cdk.core.AbstractEnvironment;
//import io.sandpipers.cdk.core.construct.BaseStack;
//import io.sandpipers.cdk.core.construct.ec2.ExistingVpc;
//import org.jetbrains.annotations.NotNull;
//import software.amazon.awscdk.services.ec2.IVpc;
//
//public class PublicIngressPrivateEgressAppRunnerServiceStack extends BaseStack {
//
//  public PublicIngressPrivateEgressAppRunnerServiceStack(@NotNull final AbstractApp app, @NotNull final AbstractEnvironment environment) {
//    super(app, environment);
//
//    final IVpc vpc = retrieveVpc(environment.getEnvironmentName());
//
//    final PublicIngressPrivateEgressAppRunnerServiceProps appRunnerServiceProps;
//    appRunnerServiceProps = PublicIngressPrivateEgressAppRunnerServiceProps.builder()
//        .vpc(vpc)
//        .subnetType(PUBLIC)
//        .healthCheckPath(Path.of("/actuator/health"))
//        .port(NumericString.of("8080"))
//        .healthCheckInterval(10)
//        .healthCheckTimeout(3)
//        .awsEcrImageIdentifier(AWSEcrImageIdentifier.of("public.ecr.aws/xxx/library:latest"))
//        .imageRepositoryType(SafeString.of("ECR_PUBLIC"))
//        .build();
//
//    new PublicIngressPrivateEgressAppRunnerService<>(this, SafeString.of(app.getApplicationName()), appRunnerServiceProps);
//  }
//
//  private IVpc retrieveVpc(final SafeString environment) {
//
//    final String vpcName = "%s-vpc".formatted(environment.getValue());
//
//    final ExistingVpc.VpcProps vpcProps = ExistingVpc.VpcProps.builder()
//        .vpcName(vpcName)
//        .build();
//
//    return new ExistingVpc(this, SafeString.of(vpcName), vpcProps).getVpc();
//  }
//}
