package Customer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Network.Protocol;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tableClass.User;


public class ChangeNickName extends Application {
	
	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
	private Customer parent;
	private User user;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button change;

    @FXML
    private Button exit;

    @FXML
    private TextField nickname;
    
    void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    void setUser(User user)
    {
    	this.user = user;
    }

    @FXML
    void change(MouseEvent event) {
    	String nick = nickname.getText().toString();
    	
    	if(nick.equals(""))
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("내용을 입력해주세요.");
			alert.showAndWait();
    	}
    	
    	else if(nick.length() > 10)
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("10자리 이내로 입력해 주세요.");
			alert.showAndWait();
    	}
    	
    	else
    	{
    		user.setNickName(nick);
    		
    		try
			{
    			p = new Protocol(15, 1, 0, user);
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
    		
    		if(p.getMainType() == 15 && p.getSubType() == 2)
    		{
    			if(p.getCode() == 1)
        		{
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("성공");
        			alert.setHeaderText(null);
        			alert.setContentText("닉네임이 변경되었습니다!");
        			alert.showAndWait();
        			
        			parent.setNickName(user.getNickName());
        			
        			Stage mainStage = (Stage)exit.getScene().getWindow();
        			mainStage.close();
        		}
    			
    			else if(p.getCode() == 2)
    			{
    				Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("에러");
        			alert.setHeaderText(null);
        			alert.setContentText("중복된 닉네임 입니다.");
        			alert.showAndWait();
    			}
        		
        		else	//error
        		{
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("에러");
        			alert.setHeaderText(null);
        			alert.setContentText("변경이 정상적으로 이루어지지 않았습니다.");
        			alert.showAndWait();
        		}
    		}
    	}
    }
    
//    String getNickname() { return nicknameValue; }
    void setParent(Customer parent) { this.parent = parent; }

    @FXML
    void close(MouseEvent event) {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
    }

    @FXML
    void initialize() {
        assert change != null : "fx:id=\"change\" was not injected: check your FXML file 'nickname.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'nickname.fxml'.";
        assert nickname != null : "fx:id=\"nickname\" was not injected: check your FXML file 'nickname.fxml'.";


    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 자동 생성된 메소드 스텁
		Parent root = FXMLLoader.load(getClass().getResource("nickname.fxml"));
    	Scene scene = new Scene(root, 350, 100);
    	primaryStage.setTitle("ChangeNickName");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
	}
}
