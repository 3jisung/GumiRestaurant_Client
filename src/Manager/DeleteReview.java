package Manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import Network.Protocol;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tableClass.*;
import Customer.*;

public class DeleteReview {
	
	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
	private LookReview parent;
	private String[] review;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_del;

    @FXML
    private Button exit;

    public void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    public void setParent(LookReview parent) { this.parent = parent; }
    
    public void setReview(String[] review) {this.review = review;}

    @FXML
    void close(MouseEvent event) {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
    }

    @FXML
    void deleteReview(MouseEvent event) {
    	try
		{
    		p = new Protocol(22, 1, 0, Integer.parseInt(review[3]));
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

		if(p.getMainType() == 22 && p.getSubType() == 2)
		{
			if(p.getCode() == 1)
			{    		
				Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("성공");
    			alert.setHeaderText(null);
    			alert.setContentText("리뷰가 삭제되었습니다!");
    			alert.showAndWait();
				
    			parent.printReview();
    			parent.updateStarPoint();
    			
    			Stage mainStage = (Stage)exit.getScene().getWindow();
    			mainStage.close();
			}
			else
			{
				Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("에러");
    			alert.setHeaderText(null);
    			alert.setContentText("리뷰 삭제가 정상적으로 이루어지지 않았습니다.");
    			alert.showAndWait();
			}
		}
    }

    @FXML
    void initialize() {
        assert btn_del != null : "fx:id=\"btn_del\" was not injected: check your FXML file 'deletereview.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'deletereview.fxml'.";


    }

}