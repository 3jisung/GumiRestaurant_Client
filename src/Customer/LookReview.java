package Customer;

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
import Manager.*;

public class LookReview {
	
	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
	private Restaurant res;
	private String[] rev;
	private String[][] result;
	private User user;
	private ViewRestaurants parent;
	private int resIndex;
	private int sortMode;
	
	private LookReview me;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn1;

    @FXML
    private Button exit;
    
    @FXML
    private TextArea resDetail;

    @FXML
    private Label resName;

    @FXML
    private ListView<String> listview;

    void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    void setUser(User user) { this.user = user;}
    
    void setRes(Restaurant res, int index) { this.res = res; this.resIndex = index; }
    
    void setParent(ViewRestaurants parent) { this.parent = parent; }
    
    void setSortMode(int sortMode) {this.sortMode = sortMode;}
    
    void printInfo()
    {
    	DecimalFormat form = new DecimalFormat("#.##");	//소수점 2자리수까지 표현
		
		resName.setText(res.getRestaurantName());
    	resDetail.setText(res.getAddress() + "\n" + res.getDistrict() + "\n"
    			+ res.getContactAddress() + "\n평균 별점 : " + form.format(res.getAverStarPoint()));
    }
    
    public void updateStarPoint()
    {
    	try
		{
    		p = new Protocol(14, 1, 0, res);
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

		if(p.getMainType() == 14 && p.getSubType() == 2)
		{
			if(p.getCode() == 1)
			{
				res = (Restaurant)p.getBody();
				printInfo();				
				parent.viewList();
			}	
		}
		else
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("에러");
			alert.setHeaderText(null);
			alert.setContentText("별점 최신화 도중 오류가 발생하였습니다.");
			alert.showAndWait();
		}
    }
    
    public void printReview() 
    {
    	try
		{
    		p = new Protocol(12, 1, 0, res);
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

		if(p.getMainType() == 12 && p.getSubType() == 2)
		{
			if(p.getCode() == 1)
			{
				result = (String[][])p.getBody();
				
				rev = new String[result.length];
		    	
		    	for(int i = 0; i < rev.length; i++)
		    	{
//		    		if(result[i][0].equals(user.getNickName()))
		    			
		    		rev[i] = result[i][0] + "\n" + result[i][1] + "\n"
		    				+ result[i][2];
		    	}
		    	
		    	  // 배열을 리스트로 변환
		        List<String> list = Arrays.asList(rev);

		        // 리스트 뒤집어 주기
		        Collections.reverse(list);

		        // 리스트를 배열로 다시 변환
		        rev = list.toArray(new String[list.size()]);
		    	
		        listview.getItems().clear();
		    	listview.getItems().addAll(rev);
			}
			else
			{
				Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("에러");
    			alert.setHeaderText(null);
    			alert.setContentText("리뷰를 불러오는데 차질이 생겼습니다.");
    			alert.showAndWait();
			}
		} 		
    }

    @FXML
    void close(MouseEvent event) {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
    }

    @FXML
    void signUp(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("signupreview.fxml"));
    	Parent root;
		root = (Parent)loader.load();
		Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.setTitle("SignUpReview");
    	stage.setScene(scene);
    	SignUpReview sur = (SignUpReview)loader.getController();
    	sur.setProtocolTool(writer, reader, socket);
    	sur.setParent(this);
    	sur.setUser(user);
    	sur.setRes(res);
    	stage.show();
    }

    @FXML
    void initialize() {
        assert btn1 != null : "fx:id=\"btn1\" was not injected: check your FXML file 'lookreview.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'lookreview.fxml'.";
        assert resDetail != null : "fx:id=\"resDetail\" was not injected: check your FXML file 'lookreview.fxml'.";
        assert resName != null : "fx:id=\"resName\" was not injected: check your FXML file 'lookreview.fxml'.";
        assert listview != null : "fx:id=\"listview\" was not injected: check your FXML file 'lookreview.fxml'.";

        me = this;
        
        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent event) {
    			try {
    				if(user.getSeparaterUser() == 2 &&
    						listview.getSelectionModel().getSelectedItem().toString() != null)
    				{
    					FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/deletereview2.fxml"));
    	            	Parent root;
    					root = (Parent)loader.load();
    					Scene scene = new Scene(root);
    	            	Stage stage = new Stage();
    	            	stage.initModality(Modality.APPLICATION_MODAL);
    	            	stage.setResizable(false);
    	            	stage.setTitle("Review");
    	            	stage.setScene(scene);
    	            	DeleteReview dr = (DeleteReview)loader.getController();
    	            	dr.setProtocolTool(writer, reader, socket);
    		        	
    		        	int index = listview.getSelectionModel().getSelectedIndex();
    		        	dr.setReview(result[result.length - 1 - index]);
    		        	dr.setParent(me);

    		        	listview.getSelectionModel().clearSelection();
    	            	stage.show();
    				}
				} catch (NullPointerException e) { }
    			catch (IOException e) {
					// TODO 자동 생성된 catch 블록
					e.printStackTrace();
				}
			}
        });
    }
}
