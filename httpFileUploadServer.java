import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
public class httpFileUploadServer{

	public static void main (String[] args) throws Exception{

		SocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("mychin", 
			new httpServerCodec());
		//acceptor.getFilterChain().addLast("mychin2",
		//	new ProtocolCodecFilter(new PrefixedStringCodecFactory()));
		acceptor.setHandler(new httpServerIoHandler());
		int bindport = 9988;
		acceptor.bind(new InetSocketAddress(bindport));
		System.out.println("Mina Server is Listing on: = " + bindport);
	}
}

