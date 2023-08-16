package com.exmple.task.dto.response;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  private int statusCode;
  private String message;
  private String description;


  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof ErrorResponse)) return false;
    final ErrorResponse that = (ErrorResponse) o;
    return statusCode == that.statusCode
        && Objects.equals(message, that.message)
        && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, message, description);
  }

  @Override
  public String toString() {
    return "ErrorResponse{"
        + "statusCode="
        + statusCode
        + ", message='"
        + message
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }

  public static ErrorResponse.ErrorResponseBuilder builder() {
    return new ErrorResponse.ErrorResponseBuilder();
  }

  public static class ErrorResponseBuilder {
    private int statusCode;
    private String message;
    private String description;

    ErrorResponseBuilder() {}

    public ErrorResponse.ErrorResponseBuilder statusCode(final int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    public ErrorResponse.ErrorResponseBuilder message(final String message) {
      this.message = message;
      return this;
    }

    public ErrorResponse.ErrorResponseBuilder description(final String description) {
      this.description = description;
      return this;
    }

    public ErrorResponse build() {
      return new ErrorResponse(this.statusCode, this.message, this.description);
    }

    public String toString() {
      return "ErrorResponse.ErrorResponseBuilder(statusCode="
          + this.statusCode
          + ", message="
          + this.message
          + ", description="
          + this.description
          + ")";
    }
  }

}
