import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class httpClientDecoder implements ProtocolDecoder{

	// Define some keys for indicating the decoding process
	private static final String DECODER_STATE_ATT = "http.ds";

	private static final String PARTIAL_HEAD_ATT = "http.ph";

	private static final String BODY_REMAINNING_BYTES = "http.brb";

	// Regex to parase HttpRequests 
	public static final Pattern REQUEST_LINE_PATTERN = Pattern.compile(" ");

	public static final Pattern KEY_VALUE_PATTERN = Pattern.compile("=");

	public static final Pattern RAW_VALUE_PATTERN = Pattern.compile("\\r\\n\\r\\n");

	public static final Pattern HEADERS_BODY_PATTERN = Pattern.compile("\\r\\n");

	public static final Pattern HEADER_VALUE_PATTERN = Pattern.compile(":");

	public static final Pattern CONTENT_BODY_PATTERN = Pattern.compile("\\r\\n");


	public void decode(IoSession session, IoBuffer msg, ProtocolDecoderOutput out){

			DecoderState state = (DecoderState) session.getAttribute(DECODER_STATE_ATT);
 			
 			if(null == state) {
 				session.getAttribute(DECODER_STATE_ATT, DecoderState.NEW);
 				state = (DecoderState)session.getAttribute(DECODER_STATE_ATT);
 			}
			
			switch(state){
 			case HEAD:
 					final ByteBuffer oldBuffer = (ByteBuffer) session.getAttribute(PARTIAL_HEAD_ATT);
 					msg  = IoBuffer.allocate(oldBuffer.remaining()+msg.remaining())
 							.put(oldBuffer).put(msg).flip();

 			case NEW:
 					httpResponse rp =  parseHttpResponse(msg.buf());
 					if (rp == null )
 					{
 						ByteBuffer partial = ByteBuffer.allocate(msg.remaining());
 						partial.put(msg.buf());
 						partial.flip();
 						session.setAttribute(PARTIAL_HEAD_ATT,partial);
 						session.setAttribute(DECODER_STATE_ATT,DecoderState.HEAD);
 						break;
 					}
 					else 
 					{
 						out.write(rp);
 						String contentLen = rp.getHeader("content-length");
 						if (contentLen != null)
 						{
 							session.setAttribute(BODY_REMAINNING_BYTES,Integer.valueOf(contentLen));
 							session.setAttribute(DECODER_STATE_ATT,DecoderState.BODY);
 						}
 						else
 						{
 							session.setAttribute(DECODER_STATE_ATT,DecoderState.NEW);
 							//out.write(new httpEndOfContent());
 							break;
 						}
 					}
 					
 			case BODY:
 					final int chunkSize = msg.remaining();

 					if(chunkSize != 0)
 					{
 							final IoBuffer wb = IoBuffer.allocate(msg.remaining());
 							wb.put(msg);
 							wb.flip();
 							out.write(wb);
 					}
 					msg.position(msg.limit());

 					int remaining = (Integer) session.getAttribute(BODY_REMAINNING_BYTES);
 					remaining -= chunkSize;

 					if(remaining <=0)
 					{
 						session.setAttribute(DECODER_STATE_ATT, DecoderState.NEW);
 						session.removeAttribute(BODY_REMAINNING_BYTES);
 						out.write(new httpEndOfContent());
 					} else
 					{
 						session.setAttribute(BODY_REMAINNING_BYTES, Integer.valueOf(remaining));
 					}
 					break;
			}
	}

	public void finishDecode(final IoSession session, final ProtocolDecoderOutput out) throws Exception{

	}

	public void dispose(final IoSession session) throws Exception{}

	private httpResponse parseHttpResponse(final ByteBuffer buffer){

			final String raw = new String(buffer.array(),0,buffer.limit());
			final String[] headerAndBody = RAW_VALUE_PATTERN.split(raw,-1);

			if (headerAndBody.length <=1)
					return null;

			String[] serverFeedBacks = CONTENT_BODY_PATTERN.split(headerAndBody[1]);
			serverFeedBacks = ArrayUtil.dropFromEndWhile(serverFeedBacks,"");
			final String serverMessage = serverFeedBacks[0];

			String[] headerFields = HEADERS_BODY_PATTERN.split(headerAndBody[0]);
			headerFields = ArrayUtil.dropFromEndWhile(headerFields,"");

			final String statusLine = headerFields[0];
			httpStatus status = null;
			String[] statusElements = REQUEST_LINE_PATTERN.split(statusLine);
			statusElements = ArrayUtil.dropFromEndWhile(statusElements,"");
         	final int statusCode = Integer.valueOf(statusElements[1]);
         	for (int i = 0; i < httpStatus.values().length; i++) {
             	status = httpStatus.values()[i];
             	if (statusCode == status.code()) {
                 	break;
             	}
         	}

			final Map<String, String> generalHeaders = new HashMap<String, String>();

			for (int i = 1; i < headerFields.length; i++){
					final String[] header = HEADER_VALUE_PATTERN.split(headerFields[i]);
					generalHeaders.put(header[0].toLowerCase(), header[1].trim());	
			}
			
			buffer.position(headerAndBody[0].length()+headerAndBody[1].length()+4);
			
			return new httpResponse(status,generalHeaders, serverMessage);

	}







}


















