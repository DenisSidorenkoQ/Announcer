package com.example.platformservice.entity.responce;

import java.util.Objects;

public class ErrorResponse {
  private int statusCode;
  private String message;
  private String description;

  public ErrorResponse() {}

  public ErrorResponse(final int statusCode, final String message, final String description) {
    this.statusCode = statusCode;
    this.message = message;
    this.description = description;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

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
