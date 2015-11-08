

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class httpClientEncoder implements ProtocolEncoder
{
	//private static final Logger LOG = 
	//		LoggerFactory.getLogger(httpClientCodec.class);
	private static final CharsetEncoder ENCODER = 
			Charset.forName("UTF-8").newEncoder();

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception
	{
			//LOG.debug("encode {}", message.getClass().getCanonicalName());

			if (message instanceof httpRequest)
			{
					httpRequest msg = (httpRequest)message;
					StringBuilder sb = new StringBuilder(msg.getMethod().toString());
					sb.append(" ");
					sb.append(msg.getRequestPath());
					if (!"".equals(msg.getQueryString())){
							sb.append("?");
							sb.append(msg.getQueryString());
					}
					sb.append(" ");
					sb.append(msg.getProtocolVersion());
					sb.append("\r\n");

					for (Map.Entry<String, String> header: msg.getHeaders().entrySet()){
							sb.append(header.getKey());
							sb.append(":  ");
							sb.append(header.getValue());
							sb.append("\r\n");
					}
					sb.append("\r\n");
					
					sb.append(msg.getFileName());
					sb.append("\r\n");
					sb.append(msg.getFileInString());
					sb.append("\r\n");
					IoBuffer buf = IoBuffer.allocate(sb.length()).setAutoExpand(true);
					buf.putString(sb.toString(), ENCODER);
					buf.flip();
					out.write(buf);
			} else if (message instanceof ByteBuffer)
			{
					out.write(message);
			} else if (message instanceof httpEndOfContant)
			{
				// end of HTTP content;
			}

	}

		public void dispose(IoSession arg0) throws Exception {
 		// TODO Auto-generated method stub
  }

}











