
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class httpRequest{

	private final httpVersion version;

	private final httpMethod method;

	private final String requestedPath;

	private final String queryString;

	private final Map<String, String> headers;

	private final String fileName;

	private final String fileInString;

	
	public httpRequest(httpVersion version, httpMethod method,
			String requestedPath, String queryString, Map<String, String> headers,
			String fileName, String fileInString)
	{
			this.version = version;
			this.method = method;
			this.requestedPath = requestedPath;
			this.queryString = queryString;
			this.headers = headers;
			this.fileName = fileName;
			this.fileInString = fileInString;
	}

	public httpVersion  getProtocolVersion(){
			return version;
	}

	public String getContentType(){
			return headers.get("content-type");
	}

	public boolean isKeepAlive() {
		return false;
	}

	public String getFileName(){
		return fileName;
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

	public boolean containsParameter(String name){
		Matcher m = parameterPatten(name);
		return m.find();
	}

	public String getParameter(String name){
		Matcher m = parameterPatten(name);
		if(m.find())
		{
			return m.group(1);
		}
		else {
			return null;
		}
	}

	protected Matcher parameterPatten(String name){
		return Pattern.compile("[&]" + name +" =([^&]*)").matcher("&"+ queryString);
	}

	public String getQueryString(){
		return queryString;
	}

	public httpMethod getMethod(){
		return method;
	}

	public String getRequestPath(){
		return requestedPath;
	}

	public String getFileInString(){
		return fileInString;
	}

	public Map<String, List<String>> getParameters(){
		Map<String, List<String>> parameters = 
				new HashMap<String, List<String>>();
		String[] params = queryString.split("&");
		if (params.length == 1){
				return parameters;
		}

		for (int i = 0; i < params.length; i++){
				String[] param = params[i].split("=");
				String name = param[0];
				String value = param.length == 2? param[1]:"";
				if(!parameters.containsKey(name)){
					parameters.put(name, new ArrayList<String>());
				}
				parameters.get(name).add(value);
		}
		return parameters;
	}

	public String toString(){
		String result =  "HTTP REQUEST METHOD: " + method + "\n";
		result += "VERSION: " + version +"\n";
		result += "REQUESTPATH: " + requestedPath +"\n";
		result += "QUERY: " + queryString +"\n";

		result += "--- HEADER --- \n";
		for(String key: headers.keySet()){
			String value = headers.get(key);
			result += key +": " + value +"\n";
		}

		result += "--- PARAMETERS --- \n";
		Map<String, List<String>> parameters = getParameters();

		for (String key: parameters.keySet()){
			Collection<String> values = parameters.get(key);
			for (String value:values) 
			{
				result += key+ ": " +value + "\n";
			}
		}

		result += "--- FILENAME --- \n";
		result += fileName+"\n";

		result += "--- FILECONTENT---\n";
		result += fileInString+"\n";
		return result;

 	}




}
