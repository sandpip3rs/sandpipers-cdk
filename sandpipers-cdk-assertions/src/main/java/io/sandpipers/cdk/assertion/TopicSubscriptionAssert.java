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
 * Fluent assertions for <code>AWS::SNS::Subscription</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsTopicSubscription(String)}.
 */
@SuppressWarnings("unchecked")
public class TopicSubscriptionAssert extends
    AbstractCDKResourcesAssert<TopicSubscriptionAssert, Map<String, Object>> {

  private TopicSubscriptionAssert(final Map<String, Object> actual) {
    super(actual, TopicSubscriptionAssert.class);
  }

  public static TopicSubscriptionAssert assertThat(final Map<String, Object> actual) {
    return new TopicSubscriptionAssert(actual);
  }

  public TopicSubscriptionAssert hasTopicArn(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> actualTopicArn = (Map<String, Object>) properties.get("TopicArn");

    Assertions.assertThat(actualTopicArn.get("Ref").toString())
        .matches(expected);

    return this;
  }

  public TopicSubscriptionAssert hasProtocol(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String actual = (String) properties.get("Protocol");

    Assertions.assertThat(actual)
        .matches(expected);

    return this;
  }

  public TopicSubscriptionAssert hasEndpoint(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String actual = (String) properties.get("Endpoint");

    Assertions.assertThat(actual)
        .matches(expected);

    return this;
  }

  public TopicSubscriptionAssert hasDeadLetterQueue(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> redrivePolicy = (Map<String, Object>) properties.get("RedrivePolicy");
    final Map<String, Object> deadLetterTargetArn = (Map<String, Object>) redrivePolicy.get("deadLetterTargetArn");
    final List<String> deadLetterTargetArnFun = (List<String>) deadLetterTargetArn.get("Fn::GetAtt");

    Assertions.assertThat(deadLetterTargetArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .containsPattern(expected));

    return this;
  }
}
