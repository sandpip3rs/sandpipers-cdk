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

package io.sandpipers.cdk.example.sns;

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;
import static io.sandpipers.cdk.core.AbstractApp.tagStackResources;
import static io.sandpipers.cdk.example.sns.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import io.sandpipers.cdk.core.util.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Template;

public class FifoTopicTest extends TemplateSupport {

  @BeforeAll
  static void initAll() {

    final Application app = new Application();

    final FifoTopicStack fifoTopicStack = new FifoTopicStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagStackResources(fifoTopicStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, CostCentre.SANDPIPERS, app.getApplicationName());

    template = Template.fromStack(fifoTopicStack);
  }

  @Test
  void should_have_topic() {

    assertThat(template).containsTopic("^Topic[a-zA-Z0-9]{8}$").hasTopicName("^SandpipersSnsCdkExampleStake-Topic-[a-zA-Z0-9]{8}.fifo$").isFifo(true)
        .hasContentBasedDeduplicationEnabled(true).hasMessageRetentionPeriodInDays(30).hasMasterKey("^Key[a-zA-Z0-9]{8}$")
        .hasTag("COST_CENTRE", "sandpipers").hasTag("ENVIRONMENT", TEST).hasTag("APPLICATION_NAME", "sns-cdk-example");
  }

  @Test
  void should_have_topic_http_subscription_with_dead_letter_topic() {
    assertThat(template).containsTopicSubscription("^Subscription[a-zA-Z0-9]{8}$").hasTopicArn("^Topic[a-zA-Z0-9]{8}$").hasProtocol("sqs")
        .hasEndpoint("http://example.com").hasDeadLetterQueue("^DeadLetterQueue[a-zA-Z0-9]{8}$");
  }
}
