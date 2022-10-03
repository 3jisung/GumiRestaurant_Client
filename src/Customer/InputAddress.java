package Customer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Network.Protocol;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tableClass.*;


public class InputAddress extends Application {

	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
	private SignUp parent;
	private String address;
	
	private String[] gumiArea = {"거의동", "고아읍", "공단동", "광평동", "구평동", "구포동", "금전동",
			"남통동", "도개면", "도량동", "무을면", "봉곡동", "부곡동", "비산동", "사곡동", "산동면",
			"상모동", "선기동", "선산읍", "선주원남동", "송정동", "수점동", "시미동", "신동", "신평동",
			"양포동", "양호동", "오태동", "옥계동", "옥성면", "원평동", "인의동", "임수동","임오동",
			"임은동", "장천면", "지산동", "진미동", "진평동", "해평면", "형곡동", "황상동"};
	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> combo_area;

    @FXML
    private Button exit;

    @FXML
    private Button input;

    @FXML
    private TextField txt_detail;
    
    public void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    public void setParent(SignUp parent) { this.parent = parent; }


    @FXML
    void close(MouseEvent event) {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
    }
    
    @FXML
    void select(ActionEvent event) {
    }

    @FXML
    void inputAddress(MouseEvent event) {    	
    	String area = combo_area.getSelectionModel().getSelectedItem();
    	String detail = txt_detail.getText().toString();   	
    	address = "경상북도 구미시 " + area + " " + detail;
    	
    	if(area == null || detail.equals(""))
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("주소를 입력해주세요.");
			alert.showAndWait();
    	}
    	
    	else if(address.length() > 45)
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("상세주소가 너무 깁니다. 도합 45자 내외가 되도록 입력해주세요.");
			alert.showAndWait();
    	}
    	
    	else
    	{
    		parent.setAddress(address);
        	
        	Stage mainStage = (Stage)exit.getScene().getWindow();
    		mainStage.close();
    	}
    }

    @FXML
    void initialize() {
        assert combo_area != null : "fx:id=\"combo_area\" was not injected: check your FXML file 'inputaddress.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'inputaddress.fxml'.";
        assert input != null : "fx:id=\"input\" was not injected: check your FXML file 'inputaddress.fxml'.";
        assert txt_detail != null : "fx:id=\"txt_detail\" was not injected: check your FXML file 'inputaddress.fxml'.";

        combo_area.getItems().addAll(gumiArea);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 자동 생성된 메소드 스텁
		Parent root = FXMLLoader.load(getClass().getResource("inputaddress.fxml"));
    	Scene scene = new Scene(root, 400, 150);
    	primaryStage.setTitle("InputAddress");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
	}
}