package Customer;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import Customer.*;
import Manager.*;
import Network.Protocol;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import tableClass.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Login extends Application{
	
	private static Protocol p;
	private static Socket socket;
	private static OutputStream os;
	private static ObjectOutputStream writer;
	private static InputStream is;
	private static ObjectInputStream reader;
	private static String host;
	private String s = "";
	
	 @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private Button cancel;

	    @FXML
	    private Button ok;

	    @FXML
	    private TextField t1;

	    @FXML
	    private PasswordField t2;
	    
	    @FXML
	    private Label title;
	    
	    @FXML
	    private Button sign;

	    public void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
	    {
	    	socket = sk;
			writer = oos;
			reader = ois;
	    }

	    @FXML
	    void click1(MouseEvent event) throws Exception{
	    	User user;
			try
			{
				p = new Protocol<User>(1, 1, new User(t1.getText(), t2.getText()));
				writer.writeObject(p);
				writer.flush();
				writer.reset();
				p = (Protocol)reader.readObject();

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if(p.getMainType() == 1 && p.getSubType() == 2)
			{
				if(p.getCode() != 3)
				{
					user = (User)p.getBody();
					if(user.getSeparaterUser() == 1) {
						Stage mainStage = (Stage)ok.getScene().getWindow();
			    		mainStage.close();
			    		FXMLLoader loader = new FXMLLoader(getClass().getResource("customer.fxml"));
			        	Parent root = (Parent)loader.load();
			        	Scene scene = new Scene(root);
			        	Stage stage = new Stage();
//			        	stage.initModality(Modality.APPLICATION_MODAL);
			        	stage.setResizable(false);
			        	stage.setTitle("Customer");
			        	stage.setScene(scene);
			        	Customer cs = (Customer)loader.getController();
			        	cs.setProtocolTool(writer, reader, socket);
			        	cs.setUser(user);
			        	
			        	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			        	    @Override
			        	    public void handle(WindowEvent t) {
			        	    	try {
			        				writer.writeObject(new Protocol(0,0));
			        				System.out.println("소켓 종료");
			        				socket.close();
			        				
			        			} catch (IOException e1) {
			        				e1.printStackTrace();
			        			}
			        	    	Platform.exit();
			        			System.exit(0);
			        	    }
			        	});
			        	
			        	stage.show();
					}
					else if(user.getSeparaterUser() == 2)
					{
						Stage mainStage = (Stage)ok.getScene().getWindow();
			    		mainStage.close();
			    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/manager.fxml"));
			        	Parent root = (Parent)loader.load();
			        	Scene scene = new Scene(root);
			        	Stage stage = new Stage();
//			        	stage.initModality(Modality.APPLICATION_MODAL);
			        	stage.setResizable(false);
			        	stage.setTitle("Manager");
			        	stage.setScene(scene);
			        	Manager mn = (Manager)loader.getController();
			        	mn.setProtocolTool(writer, reader, socket);
			        	mn.setUser(user);
			        	
			        	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			        	    @Override
			        	    public void handle(WindowEvent t) {
			        	    	try {
			        				writer.writeObject(new Protocol(0,0));
			        				System.out.println("소켓 종료");
			        				socket.close();
			        				
			        			} catch (IOException e1) {
			        				e1.printStackTrace();
			        			}
			        	    	Platform.exit();
			        			System.exit(0);
			        	    }
			        	});
			        	
			        	stage.show();
					}
					else {
						//오류
					}
				}
				else
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("에러");
					alert.setHeaderText(null);
					alert.setContentText("아이디 혹은 패스워드가 틀립니다.");
					alert.showAndWait();
				}
			}
	    }

	    @FXML
	    void click2(MouseEvent event) {
	    	try {
				writer.writeObject(new Protocol(0,0));
				System.out.println("소켓 종료");
				socket.close();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    	Platform.exit();
			System.exit(0);
	    }
	    
	    @FXML
	    void signUp(MouseEvent event) throws Exception {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
	    	Parent root = (Parent)loader.load();
	    	Scene scene = new Scene(root);
	    	Stage stage = new Stage();
	    	stage.initModality(Modality.APPLICATION_MODAL);
	    	stage.setResizable(false);
	    	stage.setTitle("SignUp");
	    	stage.setScene(scene);
	    	SignUp su = (SignUp)loader.getController();
	    	su.setProtocolTool(writer, reader, socket);
	    	stage.show();
	    }

	    @FXML
	    void initialize() {
	        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'login.fxml'.";
	        assert ok != null : "fx:id=\"ok\" was not injected: check your FXML file 'login.fxml'.";
	        assert t1 != null : "fx:id=\"t1\" was not injected: check your FXML file 'login.fxml'.";
	        assert t2 != null : "fx:id=\"t2\" was not injected: check your FXML file 'login.fxml'.";
	        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'login.fxml'.";
	    }
    
    public void start(Stage primaryStage) throws Exception {
    	Scanner sc = new Scanner(System.in);
		System.out.print("서버의 ip 입력 : ");
		host = sc.next();
		
    	socket = new Socket(host, 5000);
		os = socket.getOutputStream();
		writer = new ObjectOutputStream(os);
		is = socket.getInputStream();
		reader = new ObjectInputStream(is);
		
    	Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
    	Scene scene = new Scene(root, 400, 275);
    	primaryStage.setTitle("Login");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
    	
    	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    	    @Override
    	    public void handle(WindowEvent t) {
    	    	try {
    				writer.writeObject(new Protocol(0,0));
    				System.out.println("소켓 종료");
    				socket.close();
    				
    			} catch (IOException e1) {
    				e1.printStackTrace();
    			}
    	    	Platform.exit();
    			System.exit(0);
    	    }
    	});
    }
    
//		public static void main(String[] args) {
//			launch(args);
//		}
	}