package Customer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import Network.Protocol;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tableClass.*;


public class Customer extends Application{
	
	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
	private User user;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_nick;
    
    @FXML
    private Button btn_password;

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    @FXML
    private TextArea text_add;

    @FXML
    private TextField text_nick;

    @FXML
    private Label text_title;
    
    @FXML
    private Button exit;
    
    @FXML
    private Button btn1;

    @FXML
    private Button btn2;
    
    void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    void setUser(User user)
    {
    	this.user = user;
    	
    	text_title.setText(user.getNickName() + " 님 환영합니다~!");
    	text_nick.setText(user.getNickName());
    	text_add.setText(user.getAddress());
    }
    
    void setNickName(String nn) { text_nick.setText(nn); text_title.setText(nn + " 님 환영합니다~!");}
    void setAddress(String address) {text_add.setText(address); }
    
    @FXML
    void changeAddress(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("address.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("ChangeAddress");
    	stage.setScene(scene);
    	ChangeAddress ca = (ChangeAddress)loader.getController();
    	ca.setParent(this);
    	ca.setProtocolTool(writer, reader, socket);
    	ca.setUser(user);
    	stage.show();
    }

    @FXML
    void changeNickname(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("nickname.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("ChangeNickName");
    	stage.setScene(scene);
    	ChangeNickName cn = (ChangeNickName)loader.getController();
    	cn.setParent(this);
    	cn.setProtocolTool(writer, reader, socket);
    	cn.setUser(user);
    	stage.show();
    }
    
    @FXML
    void changePassword(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("password.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("ChangePassword");
    	stage.setScene(scene);
    	ChangePassword cp = (ChangePassword)loader.getController();
    	cp.setProtocolTool(writer, reader, socket);
    	cp.setUser(user);
    	stage.show();
    }
    
    @FXML
    void close(MouseEvent event) throws Exception {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.setResizable(false);
    	stage.setTitle("Login");
    	stage.setScene(scene);
    	Login lg = (Login)loader.getController();
    	lg.setProtocolTool(writer, reader, socket);
    	
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
    
    @FXML
    void lookRestaurants(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("restaurants.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("Restaurants");
    	stage.setScene(scene);
    	
    	ViewRestaurants vr = (ViewRestaurants)loader.getController();
    	vr.setProtocolTool(writer, reader, socket);
    	vr.setUser(user);
    	vr.viewList();
		stage.show();
		
//		ArrayList arr = new ArrayList();
//		arr.add("교촌치킨\n치킨\nAAA\nBBB\n010~\n4");
//		arr.add("@@피자\n피자\nBBB\nbbb\n010~\n3.65");
//		res.setArray(arr);
//		res.addList();
    }
    
    @FXML
    void myReview(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("managemyreview.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("ManageMyReview");
    	stage.setScene(scene);
    	ManageMyReview mr = (ManageMyReview)loader.getController();
//    	mr.setParent(this);
    	mr.setProtocolTool(writer, reader, socket);
    	mr.setUser(user);
    	mr.printMyReview();
    	stage.show();
    }

    @FXML
    void initialize() {
    	assert btn1 != null : "fx:id=\"btn1\" was not injected: check your FXML file 'customer.fxml'.";
        assert btn2 != null : "fx:id=\"btn2\" was not injected: check your FXML file 'customer.fxml'.";
        assert btn_add != null : "fx:id=\"btn_add\" was not injected: check your FXML file 'customer.fxml'.";
        assert btn_nick != null : "fx:id=\"btn_nick\" was not injected: check your FXML file 'customer.fxml'.";
        assert btn_password != null : "fx:id=\"btn_password\" was not injected: check your FXML file 'customer.fxml'.";
        assert tab1 != null : "fx:id=\"tab1\" was not injected: check your FXML file 'customer.fxml'.";
        assert tab2 != null : "fx:id=\"tab2\" was not injected: check your FXML file 'customer.fxml'.";
        assert text_add != null : "fx:id=\"text_add\" was not injected: check your FXML file 'customer.fxml'.";
        assert text_nick != null : "fx:id=\"text_nick\" was not injected: check your FXML file 'customer.fxml'.";
        assert text_title != null : "fx:id=\"text_title\" was not injected: check your FXML file 'customer.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'customer.fxml'.";
        
        tab1.setStyle("-fx-pref-width: 100px");
        tab2.setStyle("-fx-pref-width: 100px");
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 자동 생성된 메소드 스텁
		Parent root = FXMLLoader.load(getClass().getResource("customer.fxml"));
    	Scene scene = new Scene(root, 500, 500);
    	primaryStage.setTitle("Customer");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
	}
	
//	public static void main(String[] args) {
//		launch(args);
//	}

}
