import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class httpClientIoHandler extends IoHandlerAdapter 
{

		@Override
		public void sessionOpened(IoSession session) throws Exception
		{
				System.out.println("Type in HTTP POST requested file name: (test.txt for testing)\n");
				Scanner sc = new Scanner(System.in);
				String fileName = sc.nextLine();
				httpRequest rq = prepareHttpRequest(fileName);
				session.write(rq);
		} 

		@Override
		public void sessionClosed(IoSession session)
		{
				System.out.println("One client disconnected");
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception
		{	
				httpResponse rp = (httpResponse)message;
				System.out.println(rp.toString());
		}

		private httpRequest prepareHttpRequest(String name) throws Exception
		{
   	    		httpVersion version = httpVersion.fromString("HTTP/1.1");
				httpMethod method = httpMethod.POST;

				String requestPath = "/local";
				String queryString = "queryString";

				Map<String, String> headers = new HashMap<String, String>();
					headers.put("content-type", "file");

				String fileName = name;
   				String fileInString = new String(readAllBytes(get(fileName)));
		
				return new httpRequest(version, method, requestPath, queryString, 
						headers, fileName, fileInString);
		}
}









