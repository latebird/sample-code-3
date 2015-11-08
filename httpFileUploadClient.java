
import java.net.InetSocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


public class httpFileUploadClient{
	public static void main(String[] args) throws Exception{
		
		NioSocketConnector connector = new NioSocketConnector();

		connector.getFilterChain().addLast("myChin",
				new httpClientCodec());
		
		connector.setHandler(new httpClientIoHandler());
		
		connector.setConnectTimeout(10);

		ConnectFuture cf = 
				connector.connect(new InetSocketAddress("localhost", 9988));
		cf.awaitUninterruptibly();
		cf.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();

	}
}