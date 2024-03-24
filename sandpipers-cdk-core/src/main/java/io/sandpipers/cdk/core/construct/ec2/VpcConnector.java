package io.sandpipers.cdk.core.construct.ec2;

import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.construct.BaseConstruct;
import java.util.List;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apprunner.alpha.IVpcConnector;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.constructs.Construct;

@Getter
public class VpcConnector extends Construct implements BaseConstruct {

  private final IVpcConnector vpcConnector;

  public VpcConnector(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final VpcConnectorProps props) {
    super(scope, id.getValue());

    final SubnetSelection subnetSelection = createSubnetSelection(props);

    final IVpc vpc = props.getVpc();

    vpcConnector = software.amazon.awscdk.services.apprunner.alpha.VpcConnector.Builder
        .create(scope, "VpcConnector")
        .vpc(vpc)
        .vpcSubnets(subnetSelection)
        .securityGroups(props.getSecurityGroups())
        .build();
  }

  private SubnetSelection createSubnetSelection(final VpcConnectorProps props) {
    return SubnetSelection.builder()
        .subnetType(props.getSubnetType())
        .build();
  }

  @Getter
  @SuperBuilder
  public static class VpcConnectorProps {

    @NotNull
    private final IVpc vpc;

    private final SubnetType subnetType;

    @NotNull
    @Singular
    protected final List<SecurityGroup> securityGroups;
  }
}
