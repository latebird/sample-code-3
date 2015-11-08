import java.util.Map;
 
 public class httpResponse {
 
     private final httpVersion version;
 
     private final httpStatus status;
 
     private final Map<String, String> headers;
 
     public httpResponse(httpVersion version, httpStatus status, Map<String, String> headers) {
          this.version = version;
          this.status = status;
          this.headers = headers;
      }
  
      public httpVersion getProtocolVersion() {
          return version;
      }
  
      public String getContentType() {
          return headers.get("content-type");
      }
  
      public boolean isKeepAlive() {
          // TODO check header and version for keep alive
          return false;
      }
  
      public String getHeader(String name) {
          return headers.get(name);
      }
  
      public boolean containsHeader(String name) {
          return headers.containsKey(name);
      }
  
      public Map<String, String> getHeaders() {
          return headers;
      }
  
      public httpStatus getStatus() {
          return status;
      }
  
      @Override
      public String toString() {
          String result = "HTTP RESPONSE STATUS: " + status + "\n";
          result += "VERSION: " + version + "\n";
  
          result += "--- HEADER --- \n";
          for (String key : headers.keySet()) {
              String value = headers.get(key);
              result += key + ":" + value + "\n";
          }
  
          return result;
      }
  }
