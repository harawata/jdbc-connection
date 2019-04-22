/**
 * Copyright (c) 2019 Iwao AVE!
 * Licensed under the MIT license.
 * See LICENSE file in the project root for details.
 */
package net.harawata.jdbc.connection;

public class MissingDbConfigException extends RuntimeException {

  private static final long serialVersionUID = -5105951934265679463L;

  public MissingDbConfigException() {
    super();
  }

  public MissingDbConfigException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public MissingDbConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public MissingDbConfigException(String message) {
    super(message);
  }

  public MissingDbConfigException(Throwable cause) {
    super(cause);
  }

}
