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

package io.sadpipers.cdk.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.regex.Pattern;
import org.typefactory.MessageCode;
import org.typefactory.StringType;
import org.typefactory.TypeParser;


public class AWSEcrImageIdentifier extends StringType implements CharSequence {

  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 1024;
  private static final String SPECIAL_CHARS =  ".-/:";
  private static final MessageCode ERROR_MESSAGE = MessageCode.of(
      "invalid.aws.ecr.image.identifier",
      "must be a %d-%d character(s) value matching '^([0-9]{12}.dkr.ecr.[a-z-]+-[0-9]{1}.amazonaws.com/.*)|(^public.ecr.aws/.+/.+)$'".formatted(
          MIN_SIZE, MAX_SIZE)
  );

  private static final TypeParser TYPE_PARSER = TypeParser.builder()
      .messageCode(ERROR_MESSAGE)
      .acceptLettersAtoZ()
      .acceptDigits0to9()
      .acceptChars(SPECIAL_CHARS.toCharArray())
      .matchesRegex(Pattern.compile("^([0-9]{12}\\.dkr\\.ecr\\.[a-z\\-]+-[0-9]{1}\\.amazonaws\\.com/.*)|(^public\\.ecr\\.aws/.+/.+(:/.+)?)$"))
      .preserveCase()
      .forbidWhitespace()
      .convertEmptyToNull()
      .minSize(MIN_SIZE)
      .maxSize(MAX_SIZE)
      .build();

  @JsonCreator
  public static AWSEcrImageIdentifier of(final CharSequence value) {
    return TYPE_PARSER.parseToStringType(value, AWSEcrImageIdentifier::new);
  }

  @JsonValue
  public String getValue() {
    return value();
  }

  private AWSEcrImageIdentifier(final String value) {
    super(value);
  }
}
