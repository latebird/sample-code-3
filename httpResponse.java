
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class httpResponse{

	
	private final httpStatus status;

	private final Map<String, String> headers;

	private final String serverMessage;
	
	public httpResponse(httpStatus status, Map<String, String> headers, String serverMessage)
	{
			this.status = status;
			this.headers = headers;
			this.serverMessage = serverMessage;
	}

	public httpStatus getStatus(){
			return status;
	}
	public String getContentType(){
			return headers.get("content-type");
	}

	public boolean isKeepAlive() {
		return false;
	}


	public String getHeader(String name){
		return headers.get(name);
	}

	public boolean containsHeader(String name){
		return headers.containsKey(name);
	}

	public Map<String, String> getHeaders(){
		return headers;
	}

	public String getServerMessage(){
		return serverMessage;
	}

	public String toString(){

		String result = "HTTP RESPONSE STATUS: \n" + status.line();
  
        result += "--- HEADER --- \n";
       	for (String key : headers.keySet()) {
            String value = headers.get(key);
            result += key + ":" + value + "\n";
        }

		result += "--- MESSAGE FROM SERVER ---\n";
		result += serverMessage +"\n";
		return result;

 	}




}
