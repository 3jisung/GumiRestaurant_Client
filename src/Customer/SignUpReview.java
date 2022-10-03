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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tableClass.*;

public class SignUpReview extends Application {
	
	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
	private LookReview parent;
	private Restaurant res;
	private User user;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exit;
    
    @FXML
    private ToggleGroup g1;

    @FXML
    private TextArea reviewValue;
    
    @FXML
    private RadioButton rb1;

    @FXML
    private RadioButton rb2;

    @FXML
    private RadioButton rb3;

    @FXML
    private RadioButton rb4;

    @FXML
    private RadioButton rb5;
    
    void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    void setParent(LookReview parent) { this.parent = parent; }
    
    void setUser(User user) { this.user = user; }
    void setRes(Restaurant res) { this.res = res; }

    @FXML
    void close(MouseEvent event) {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
    }

    @FXML
    void createReview(MouseEvent event) {
    	String review = reviewValue.getText().toString();
    	
    	if(review.equals(""))
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("내용을 입력해주세요.");
			alert.showAndWait();
    	}
    	
    	else if(g1.getSelectedToggle() == null)
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("별점을 선택해주세요.");
			alert.showAndWait();
    	}
    	
    	else if(review.length() > 45)
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("45자리 이내로 입력해 주세요.");
			alert.showAndWait();
    	}
    	else
    	{ 	
    		String sp = g1.getSelectedToggle().getUserData().toString();
    		Double starPoint = Double.parseDouble(sp);
    		
    		Review rev = new Review();
    		rev.setUserID(user.getUserID());
    		rev.setRestaurantID(res.getRestaurantID());
    		rev.setStarPoint(starPoint);
        	rev.setContent(review);
    		
    		try
			{
    			p = new Protocol(13, 1, 0, rev);
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
    		
    		if(p.getMainType() == 13 && p.getSubType() == 2)
    		{
    			if(p.getCode() == 1)
        		{
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("성공");
        			alert.setHeaderText(null);
        			alert.setContentText("리뷰가 등록되었습니다!");
        			alert.showAndWait();
        			
        			parent.printReview();
        			parent.updateStarPoint();
        			
        			Stage mainStage = (Stage)exit.getScene().getWindow();
        			mainStage.close();
        			
//        			parent.setNickName(user.getNickName());
        		}
        		else	//error
        		{
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("에러");
        			alert.setHeaderText(null);
        			alert.setContentText("리뷰 등록이 정상적으로 이루어지지 않았습니다.");
        			alert.showAndWait();
        		}
    		}
    	}
    }

    @FXML
    void initialize() {
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'review.fxml'.";
        assert reviewValue != null : "fx:id=\"reviewValue\" was not injected: check your FXML file 'review.fxml'.";
        assert g1 != null : "fx:id=\"g1\" was not injected: check your FXML file 'signupreview.fxml'.";
        assert rb1 != null : "fx:id=\"rb1\" was not injected: check your FXML file 'signupreview.fxml'.";
        assert rb2 != null : "fx:id=\"rb2\" was not injected: check your FXML file 'signupreview.fxml'.";
        assert rb3 != null : "fx:id=\"rb3\" was not injected: check your FXML file 'signupreview.fxml'.";
        assert rb4 != null : "fx:id=\"rb4\" was not injected: check your FXML file 'signupreview.fxml'.";
        assert rb5 != null : "fx:id=\"rb5\" was not injected: check your FXML file 'signupreview.fxml'.";
    
        rb1.setUserData(1);
        rb2.setUserData(2);
        rb3.setUserData(3);
        rb4.setUserData(4);
        rb5.setUserData(5);
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 자동 생성된 메소드 스텁
		Parent root = FXMLLoader.load(getClass().getResource("review.fxml"));
    	Scene scene = new Scene(root, 350, 200);
    	primaryStage.setTitle("Review");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
	}
}
