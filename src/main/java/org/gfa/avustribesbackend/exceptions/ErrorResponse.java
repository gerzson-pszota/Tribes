package org.gfa.avustribesbackend.exceptions;

import java.util.Date;

public class ErrorResponse {

  private String message;
  private String endpoint;
  private Date time;

  public ErrorResponse(
      String message,
      String endpoint,
      Date time) {
    this.message = message;
    this.endpoint = endpoint;
    this.time = time;
  }

  public String getMessage() {
    return message;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public Date getTime() {
    return time;
  }
}
