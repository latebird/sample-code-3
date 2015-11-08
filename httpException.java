@SuppressWarnings("serial")
  public class httpException extends RuntimeException {
  
      private final int statusCode;
  
      public httpException(final int statusCode) {
          this(statusCode, "");
      }
      
      public httpException(final httpStatus statusCode) {
      	this(statusCode, "");
      }
  
      public httpException(final int statusCode, final String message) {
          super(message);
          this.statusCode = statusCode;
      }
      
      public httpException(final httpStatus statusCode, final String message) {
          super(message);
          this.statusCode = statusCode.code();
      }
  
      public int getStatusCode() {
          return statusCode;
      }
  
  }