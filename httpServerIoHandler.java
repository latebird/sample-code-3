import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class httpServerIoHandler extends IoHandlerAdapter
{
		@Override
		public void sessionOpened(IoSession session) throws Exception
		{
				System.out.println("New Session Started.");	
		}

		@Override
		public void sessionClosed(IoSession session)
		{
				System.out.println("One client disconnected");
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception
		{	
				httpRequest rq = (httpRequest)message;
				storeFile(rq);
				System.out.println(rq.toString());
				httpResponse rp = prepareHttpResponse("File uploaded successful!");
				session.write(rp);
		}

		private httpResponse prepareHttpResponse(String serverMessage) throws Exception
		{
   	    		httpStatus status = httpStatus.SUCCESS_OK; 
				Map<String, String> headers = new HashMap<String, String>();
					headers.put("content-type", "file");
				String message = serverMessage;
				return new httpResponse(status, headers, message);
		}

		private void storeFile(httpRequest rq) throws Exception
		{	
				String comments = "This is the duplicated copy of the uploaded file for the purpose of test.\n\n\n";
				byte commentsByte[] = comments.getBytes(Charset.forName("UTF-8"));
			   	byte fileBytes[] = rq.getFileInString().getBytes(Charset.forName("UTF-8"));

				FileOutputStream out = new FileOutputStream("test_store.txt",false);
				out.write(commentsByte);
				out.write(fileBytes);
				out.close();
		}

}

