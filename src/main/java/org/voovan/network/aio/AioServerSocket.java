package org.voovan.network.aio;

import org.voovan.network.EventTrigger;
import org.voovan.network.SocketContext;
import org.voovan.tools.TEnv;
import org.voovan.tools.log.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * AioServerSocket 监听
 * 
 * @author helyho
 *
 * Voovan Framework.
 * WebSite: https://github.com/helyho/Voovan
 * Licence: Apache v2 License
 */
public class AioServerSocket extends SocketContext{

	private AsynchronousServerSocketChannel serverSocketChannel;

	/**
	 * 构造函数
	 * @param host    主机地址
	 * @param port    主机端口
	 * @param readTimeout 超时时间
	 * @throws IOException IO 异常
	 */
	public AioServerSocket(String host,int port,int readTimeout) throws IOException{
		super(host, port, readTimeout);
		serverSocketChannel = AsynchronousServerSocketChannel.open();
	}
	
	/**
	 * 捕获 Aio Accept 事件
	 */
	protected void catchAccept(){
		serverSocketChannel.accept(this, new AcceptCompletionHandler());
	}
	
	@Override
	public void start() throws IOException {
		InetSocketAddress socketAddress = new InetSocketAddress(host, port);
		serverSocketChannel.bind(socketAddress, 1000);
		catchAccept();
		
		//等待ServerSocketChannel关闭,结束进程
		while(isConnect()) {
			TEnv.sleep(1);
		}
	}

	@Override
	public boolean isConnect() {
		return serverSocketChannel.isOpen();
	}
	
	@Override
	public boolean close(){
		
		if(serverSocketChannel!=null && serverSocketChannel.isOpen()){
			try{
				//触发 DisConnect 事件
				EventTrigger.fireDisconnect(null);

				//关闭 Socket 连接
				if(serverSocketChannel.isOpen()){
					serverSocketChannel.close();
				}
				return true;
			}catch(IOException e){
				Logger.error("SocketChannel close failed.",e);
				return false;
			}
		}else{
			return true;
		}
	}
}
