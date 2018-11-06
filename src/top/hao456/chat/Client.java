package top.hao456.chat;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;//
import java.util.Scanner;

/**
 * 聊天室客户端
 * @author axin
 *
 */
public class Client {
	
	/**
	 * java.net.Socket
	 * 套接字
	 * Socket 封装了TCP通讯协议,使用它可以基于TCP协议与远端计算机通讯
	 */
	
	private Socket socket;
	
	/**
	 * 客户端的构造方法用来初始化客户端
	 */
	
	public Client(){
		
		try {
			
			/**
			 * 实例化Socket 时,构造方法要求传入
			 * 两个参数:
			 * 1:String ,指定服务端的ip地址
			 * 2:int,指定服务端打开的服务端口号
			 * 通过ip地址可以找到服务端 所在计算机,
			 * 通过端口号可以找到服务器上运行的服务端应用程序
			 * 
			 * 127.0.0.1 表示自己
			 * "localhost",也表示自己
			 */
			System.out.println("正在连接服务端...");
			socket = new Socket("192.168.1.199",8088);//172.86.80.91
			System.out.println("与服务端 建立连接");
		} catch (Exception e) {
			e.printStackTrace();
			//记录日志 
		}
	}
	
	
	/**
	 * 启动客户端的方法
	 */
	public void start(){
		/*
		 * Socket提供了方法
		 * OutputStream getOutputStream()
		 * 该方法可以获取一个输出流,通过该输出流写出的数据会发送给远端,这里远端就是服务端
		 * 
		 */
		try {
			
			OutputStream out = socket.getOutputStream();
			
			OutputStreamWriter osw = new OutputStreamWriter(out,"utf-8");
			
			PrintWriter pw = new PrintWriter(osw,true);   //true代表行刷新
			
			Scanner scan = new Scanner(System.in);
			
			//启动用于接收服务端发过来消息的线程
			ServerHandler handler = new ServerHandler();
			Thread t = new Thread(handler);
			pw.flush();
			t.start();
			
			//向服务端发送信息
			
			long lastSend = System.currentTimeMillis()-1000;
			while(true){
				//System.out.println("请输入:");
				String message = scan.nextLine();
				if(System.currentTimeMillis()-lastSend<1000){
					System.out.println("说话太快了!稍等一下");
				}
				else{
					pw.println(message);
					lastSend = System.currentTimeMillis();
				}
				
				
				
				
				
				
			}
		} catch (Exception e) {
			
		}
		
	}
	
	
	public static void main(String[] args) {
		
		try{
			
			/**
			 * 实例化客户端 
			 */
			Client client = new Client();
			/**
			 * 启动客户端
			 */
			client.start();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("客户端启动失败!");
		}
	}
	
	
	
	private class ServerHandler implements Runnable{
		
		public void run(){
			
			try{
				
				InputStream in = socket.getInputStream();
				
				InputStreamReader isr = new InputStreamReader(in,"UTF-8");
				
				BufferedReader br = new BufferedReader(isr);
				
				String message = null;
				while((message = br.readLine())!=null){
					System.out.println(message);
				}
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
			
			
			
			
			
		}
	}
	
	

	
}
