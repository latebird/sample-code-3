import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class httpClientCodec extends ProtocolCodecFilter
{

		private static final String DECODER_STATE_ATT = "http.ds";
		private static final String PARTIAL_HEAD_ATT = "http.ph";

		private static ProtocolEncoder encoder = new httpClientEncoder();
		private static ProtocolDecoder decoder = new httpClientDecoder();
		public httpClientCodec(){
			super(encoder, decoder);
		}

		@Override
		public void sessionClosed(IoFilter.NextFilter NextFilter, IoSession session)
				throws Exception
		{
				super.sessionClosed(NextFilter,session);
				session.removeAttribute(DECODER_STATE_ATT);
				session.removeAttribute(PARTIAL_HEAD_ATT);
		}
}