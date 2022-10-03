package Manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Customer.*;
import Network.Protocol;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import tableClass.*;

public class Manager extends Application {
	
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
    private Button btn_DB;
    
    @FXML
    private Button btn_Rv;
    
    @FXML
    private Button exit;
    
    public void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    public void setUser(User user)
    {
    	this.user = user;
    }
    
    @FXML
    void manageReview(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/restaurants.fxml"));
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
    }

    @FXML
    void renewalDB(MouseEvent event) {
    	try
		{
    		p = new Protocol(31, 1, 0, null);
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

		if(p.getMainType() == 31 && p.getSubType() == 2)
		{
			if(p.getCode() == 1)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("성공");
    			alert.setHeaderText(null);
    			alert.setContentText("DB 최신화가 정상적으로 완료되었습니다.");
    			alert.showAndWait();
			}
			else
			{
				Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("에러");
    			alert.setHeaderText(null);
    			alert.setContentText("DB 최신화가 정상적으로 이루어지지 않았습니다.");
    			alert.showAndWait();
			}
		}
    }
    
    @FXML
    void close(MouseEvent event) throws Exception {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/login.fxml"));
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
    void initialize() {
        assert btn_DB != null : "fx:id=\"btn_DB\" was not injected: check your FXML file 'manager.fxml'.";
        assert btn_Rv != null : "fx:id=\"btn_Rv\" was not injected: check your FXML file 'manager.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'manager.fxml'.";

    }

    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 자동 생성된 메소드 스텁
		Parent root = FXMLLoader.load(getClass().getResource("Manager.fxml"));
    	Scene scene = new Scene(root, 500, 500);
    	primaryStage.setTitle("Manager");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
	}
    
//	public static void main(String[] args) {
//		launch(args);
//	}
}
