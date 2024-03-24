package io.sadpipers.cdk.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.regex.Pattern;
import org.typefactory.MessageCode;
import org.typefactory.StringType;
import org.typefactory.TypeParser;

public class ECRRepositoryType extends StringType implements CharSequence {

  private static final int MIN_SIZE = 3;
  private static final int MAX_SIZE = 10;
  private static final MessageCode ERROR_MESSAGE = MessageCode.of(
      "invalid.aws.ecr.type",
      "must be a %d-%d character(s) value matching '^ECR|ECR_PUBLIC$'".formatted(
          MIN_SIZE, MAX_SIZE)
  );

  private static final TypeParser TYPE_PARSER = TypeParser.builder()
      .messageCode(ERROR_MESSAGE)
      .acceptLettersAtoZ()
      .acceptChars("_".toCharArray())
      .matchesRegex(Pattern.compile("^ECR|ECR_PUBLIC$"))
      .preserveCase()
      .forbidWhitespace()
      .convertEmptyToNull()
      .minSize(MIN_SIZE)
      .maxSize(MAX_SIZE)
      .build();

  @JsonCreator
  public static ECRRepositoryType of(final CharSequence value) {
    return TYPE_PARSER.parseToStringType(value, ECRRepositoryType::new);
  }

  @JsonValue
  public String getValue() {
    return value();
  }

  private ECRRepositoryType(final String value) {
    super(value);
  }
}
