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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tableClass.*;


public class SignUp extends Application {

	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_ck1;

    @FXML
    private Button btn_ck2;

    @FXML
    private Button btn_sign;

    @FXML
    private Button exit;

    @FXML
    private TextArea txt_add;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_nick;

    @FXML
    private TextField txt_pwd1;

    @FXML
    private TextField txt_pwd2;
    
    @FXML
    private Label txt_check1;

    @FXML
    private Label txt_check2;
    
    public void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    public void setCheck1(String s1, String s2) { txt_check1.setText(s1); txt_id.setText(s2);}
    public void setCheck2(String s1, String s2) { txt_check2.setText(s1); txt_nick.setText(s2);}
    public void setAddress(String address) {txt_add.setText(address);}
    
    @FXML
    void check1(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("inputid.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("InputID");
    	stage.setScene(scene);
    	InputID ip = (InputID)loader.getController();
    	ip.setParent(this);
    	ip.setProtocolTool(writer, reader, socket);
    	stage.show();
    }

    @FXML
    void check2(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("inputnickname.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("InputNickName");
    	stage.setScene(scene);
    	InputNickName ip = (InputNickName)loader.getController();
    	ip.setParent(this);
    	ip.setProtocolTool(writer, reader, socket);
    	stage.show();
    }

    @FXML
    void choiceAddress(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("inputaddress.fxml"));
    	Parent root = (Parent)loader.load();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("InputAddress");
    	stage.setScene(scene);
    	InputAddress ia = (InputAddress)loader.getController();
    	ia.setParent(this);
    	ia.setProtocolTool(writer, reader, socket);
    	stage.show();
    }

    @FXML
    void close(MouseEvent event) {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
    }

    @FXML
    void sign(MouseEvent event) {
    	if(txt_check1.getText().toString().equals("적합") &&
    			txt_check2.getText().toString().equals("적합") &&
    			!(txt_pwd1.getText().toString().equals("")) &&
    			txt_pwd1.getText().equals(txt_pwd2.getText()) &&
    			!(txt_add.getText().toString().equals("")) )
    	{
    		User user = new User();
    		user.setUserID(txt_id.getText().toString());
    		user.setNickName(txt_nick.getText().toString());
    		user.setPassword(txt_pwd1.getText().toString());
    		user.setSeparaterUser(1);
    		user.setAddress(txt_add.getText().toString());
    		
    		try
    		{
    			p = new Protocol(4, 1, 0, user);
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
    		
    		if(p.getMainType() == 4 && p.getSubType() == 2)
    		{
    			if(p.getCode() == 1)
    			{
    				Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("완료");
        			alert.setHeaderText(null);
        			alert.setContentText("회원가입이 완료되었습니다!");
        			alert.showAndWait();
    				
    				Stage mainStage = (Stage)exit.getScene().getWindow();
    				mainStage.close();
    			}
    			else
    			{
    				Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("에러");
        			alert.setHeaderText(null);
        			alert.setContentText("회원가입이 정상적으로 이루어지지 않았습니다.");
        			alert.showAndWait();
    			}
    		}
    	}
    	
    	else if(!(txt_pwd1.getText().equals(txt_pwd2.getText())))
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("비밀번호가 일치하지 않습니다.");
			alert.showAndWait();
    	}
    	
    	else if(txt_pwd1.getText().toString().equals(""))
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("비밀번호를 입력해 주세요.");
			alert.showAndWait();
    	}
    	
    	else
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("양식이 올바르지 않습니다.");
			alert.showAndWait();
    	}
    }

    @FXML
    void initialize() {
        assert btn_ck1 != null : "fx:id=\"btn_ck1\" was not injected: check your FXML file 'signup.fxml'.";
        assert btn_ck2 != null : "fx:id=\"btn_ck2\" was not injected: check your FXML file 'signup.fxml'.";
        assert btn_sign != null : "fx:id=\"btn_sign\" was not injected: check your FXML file 'signup.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'signup.fxml'.";
        assert txt_add != null : "fx:id=\"txt_add\" was not injected: check your FXML file 'signup.fxml'.";
        assert txt_id != null : "fx:id=\"txt_id\" was not injected: check your FXML file 'signup.fxml'.";
        assert txt_nick != null : "fx:id=\"txt_nick\" was not injected: check your FXML file 'signup.fxml'.";
        assert txt_pwd1 != null : "fx:id=\"txt_pwd1\" was not injected: check your FXML file 'signup.fxml'.";
        assert txt_pwd2 != null : "fx:id=\"txt_pwd2\" was not injected: check your FXML file 'signup.fxml'.";
        assert txt_check1 != null : "fx:id=\"txt_check1\" was not injected: check your FXML file 'signup.fxml'.";
        assert txt_check2 != null : "fx:id=\"txt_check2\" was not injected: check your FXML file 'signup.fxml'.";
    }

    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 자동 생성된 메소드 스텁
		Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
    	Scene scene = new Scene(root, 500, 500);
    	primaryStage.setTitle("SignUp");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
	}
}
