
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Client extends Application {
	PrintWriter PrintWr;
	public static void main(String[] args) {
		launch(args);
	}
	 public void start(Stage primaryStage) throws Exception{
	     primaryStage.setTitle("Client Chat 2.0.1");
	     BorderPane borderPane= new BorderPane();
	     Label labelIP=new Label("IP:");
	     TextField textfieldIP= new TextField("127.0.0.1");
	     
	     Label labelPort=new Label("Port:");
	     TextField textfieldPort= new TextField("5000");
	     
	     Button btnConnecter= new Button("Connecter");
	     
	     HBox hbox= new HBox();
	     hbox.setSpacing(10);
	     hbox.setPadding(new Insets(10));
	     //btnConnecter.setBackground(new Background(new BackgroundFill(Color.GAINSBORO,null,null)));
	     
	     hbox.getChildren().addAll(labelIP,textfieldIP,labelPort,textfieldPort,btnConnecter);
	     borderPane.setTop(hbox);
	     
	     VBox vbox2= new VBox();
	     vbox2.setSpacing(10);
	     vbox2.setPadding(new Insets(10));
	     ObservableList<String> listModel=FXCollections.observableArrayList();
	     ListView<String> listView= new ListView<String>(listModel); 
	     vbox2.getChildren().add(listView);
	     borderPane.setCenter(vbox2);
	     
	     Label labelMessage=new Label("Message");
	     TextField textFiedMessage= new TextField();
	     textFiedMessage.setPrefSize(430,30);
	     Button buttonEnv=new Button("Envoyer");
	
	     HBox hbox2= new HBox();
	     hbox2.setSpacing(10);
	     hbox2.setPadding(new Insets(10));
	     hbox2.getChildren().addAll(labelMessage,textFiedMessage,buttonEnv);
	     
	     borderPane.setBottom(hbox2);
	     
	     Scene scene=new Scene(borderPane,600,400);
	     primaryStage.setScene(scene);
		 primaryStage.show();
		 
		 btnConnecter.setOnAction((evt)->{
			 String IP=textfieldIP.getText();
			 int Port=Integer.parseInt(textfieldPort.getText());
			 
			 try{
				 Socket socket=new Socket(IP,Port);
				 InputStream inputStream= socket.getInputStream();
				 InputStreamReader inSRead= new InputStreamReader(inputStream);
				 BufferedReader bufferedReader= new BufferedReader(inSRead);
				 PrintWr= new PrintWriter(socket.getOutputStream(),true);
				 new Thread(()->{
					 
						 while(true){
							 try{
							 String response=bufferedReader.readLine();			
								 listModel.add(response);
						 }catch (IOException e){
							 e.printStackTrace();
						 }
					 }
				 }).start();
			 }catch(IOException e){
				 e.printStackTrace();
			 }
		 });
		 buttonEnv.setOnAction((evt)->{
			 String message= textFiedMessage.getText();
			 PrintWr.println(message);
		 });

	    }
}

	    
