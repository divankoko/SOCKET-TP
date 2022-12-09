
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Serveur extends Thread{
	private int nbrClients=0;
	private ArrayList<Conversation> clients = new ArrayList<Conversation>();

	public void run(){
		try{
			@SuppressWarnings("resource")
			ServerSocket serverSocket= new ServerSocket(5000);
			while(true){
				Socket socket=serverSocket.accept();
				++nbrClients;
				Conversation conversation= new Conversation(socket,nbrClients);
				clients.add(conversation);
				conversation.start();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	class Conversation extends Thread{
		protected Socket  socketClient;
		protected int numero;
		public Conversation(Socket socketClient, int numero){
			this.socketClient=socketClient;
			this.numero=numero;
		}
		public void broadcastMessage(String message, Socket socket,int numClient){
		try{
			for(Conversation client:clients){
				if(client.socketClient!=socket){
					if(client.numero==numClient || numClient==-1){
						PrintWriter printwr=new PrintWriter(client.socketClient.getOutputStream(),true);
						printwr.println(message);
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		}
		public void run(){
			try{
			InputStream inputStream= socketClient.getInputStream();
			InputStreamReader isr= new InputStreamReader(inputStream);
			BufferedReader br= new BufferedReader(isr);
			
			PrintWriter pw= new PrintWriter(socketClient.getOutputStream(),true);
			String ipClient=socketClient.getRemoteSocketAddress().toString();
			pw.println("Connexion etablie Client numero"+numero);
			System.out.println("Connexion du client Numero"+numero+", IP="+ipClient);
			
			while(true){
				String req=br.readLine();
				
				if(req.contains("=>")){
					String[] requestParams=req.split("=>");
					if(requestParams.length==2);
						String message=requestParams[1];
						int numeroClient=Integer.parseInt(requestParams[0]);
						broadcastMessage(message,socketClient,numeroClient);
		 			}else{
						broadcastMessage(req,socketClient,-1);}
					}
				
			
		}catch(IOException e){
		e.printStackTrace();	
		}
		}
			
			}
	
	
	
	public static void main(String[] args) {
		new Serveur().start();
	}

}
