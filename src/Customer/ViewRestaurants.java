package Customer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import Network.Protocol;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tableClass.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ViewRestaurants extends Application{
	
	private static Protocol p;
	private static Socket socket;
	private static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	
	private Restaurant[] res;
	private String[] arr;
	private User user;
	private ViewRestaurants me;
	private int sortMode;	// 0 : 노말, 1 : 별점순 정렬
	private int nearMode;	// 0 : 노말, 1 : 인근지역 필터링

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Button btn_dis;

    @FXML
    private Button btn_nm;

    @FXML
    private Button btn_sp;

    @FXML
    private Button exit;

    @FXML
    private ListView<String> listview;

    public void setProtocolTool(ObjectOutputStream oos, ObjectInputStream ois, Socket sk)
    {
    	socket = sk;
		writer = oos;
		reader = ois;
    }
    
    public void setUser(User user) { this.user = user;}
    
    public void viewList()
    {
    	try
		{
			p = new Protocol(11, 1, 0, null);
			writer.writeObject(p);
			writer.flush();
			writer.reset();
			p = (Protocol)reader.readObject();
			
			if(p.getMainType() == 11 && p.getSubType() == 2)
			{
				if(p.getCode() == 1)
				{
					res = (Restaurant[])p.getBody();
					
					if(sortMode == 1)
						sortSP();
					if(nearMode == 1)
						filterDT();
					if(sortMode != 1 && nearMode != 1)
						printList();
				}
				else
				{
					Alert alert = new Alert(AlertType.INFORMATION);
	    			alert.setTitle("에러");
	    			alert.setHeaderText(null);
	    			alert.setContentText("음식점 명단을 불러오지 못하였습니다.");
	    			alert.showAndWait();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    @FXML
    void close(MouseEvent event) {
    	Stage mainStage = (Stage)exit.getScene().getWindow();
		mainStage.close();
    }
    
    @FXML
    void distanceFilter(MouseEvent event) {
    	filterDT();
    	nearMode = 1;
    }
    
    void filterDT()
    {
    	nearMode = 1;
    	
    	ArrayList<Restaurant> list = new ArrayList<>();
    	for(int i = 0; i < res.length; i++)
    	{
    		if(res[i].getDistrict() != null && user.getAddress().indexOf(res[i].getDistrict()) != -1)
    			list.add(res[i]);
    	}
    	
    	res = list.toArray(new Restaurant[list.size()]);
    	
    	printList();
    }

    @FXML
    void starPointSort(MouseEvent event) {
		sortSP();
    	sortMode = 1; 		
    }
    
    void sortSP()
    {
    	List<Restaurant> list = Arrays.asList(res);
    	
    	Collections.sort(list, new StarCompare());
    	
    	printList();
    }
    
    @FXML
    void normalSort(MouseEvent event) {
    	sortMode = 0;
    	nearMode = 0;
    	sortNM();	
    }
    
    void sortNM()
    {
    	viewList();    	
    	printList();
    }
    
    void printList()
    {
    	arr = new String[res.length];
    	
    	DecimalFormat form = new DecimalFormat("#.##");	//소수점 2자리수까지 표현
    	
    	for(int i = 0; i < arr.length; i++)
    	{
    		arr[i] = res[i].getRestaurantName() + "\n주 메뉴 : " + res[i].getMainDish()
    				+ "\n평균 별점 : " + form.format(res[i].getAverStarPoint()) + " (리뷰 수 : "
    				+ res[i].getRevAmount() + " )";
    	}
    	listview.getItems().clear();
    	listview.getItems().addAll(arr);
    }
//    void addList() { listview.getItems().addAll(arr); };

    @FXML
    void initialize() {
    	assert btn_dis != null : "fx:id=\"btn_dis\" was not injected: check your FXML file 'restaurants.fxml'.";
        assert btn_nm != null : "fx:id=\"btn_nm\" was not injected: check your FXML file 'restaurants.fxml'.";
        assert btn_sp != null : "fx:id=\"btn_sp\" was not injected: check your FXML file 'restaurants.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'restaurants.fxml'.";
        assert listview != null : "fx:id=\"listview\" was not injected: check your FXML file 'restaurants.fxml'.";
        
        me = this;
        sortMode = 0;
        nearMode = 0;
        
        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent event) {
    			try {
    				if(listview.getSelectionModel().getSelectedItem().toString() != null)
    				{
    					FXMLLoader loader = new FXMLLoader(getClass().getResource("lookreview.fxml"));
    	            	Parent root;
    					root = (Parent)loader.load();
    					Scene scene = new Scene(root);
    	            	Stage stage = new Stage();
    	            	stage.initModality(Modality.APPLICATION_MODAL);
    	            	stage.setResizable(false);
    	            	stage.setTitle("Review");
    	            	stage.setScene(scene);
    	            	LookReview lr = (LookReview)loader.getController();
    		        	lr.setProtocolTool(writer, reader, socket);
    		        	
    		        	int index = listview.getSelectionModel().getSelectedIndex();
    		        	lr.setRes(res[index], index);
    		        	lr.setSortMode(sortMode);
    		        	lr.setUser(user);
    		        	lr.setParent(me);
    		        	lr.printInfo();
    		        	lr.printReview();

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
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 자동 생성된 메소드 스텁
		Parent root = FXMLLoader.load(getClass().getResource("restaurants.fxml"));
    	Scene scene = new Scene(root, 500, 500);
    	primaryStage.setTitle("Restaurants");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    	primaryStage.getScene().getWindow().requestFocus();
	}
}

class StarCompare implements Comparator<Restaurant> {
    int ret = 0;
    
    @Override
    public int compare(Restaurant s1, Restaurant s2) {
        if(s1.getAverStarPoint().compareTo(s2.getAverStarPoint()) > 0) {
            ret = -1;
        }
        else if(s1.getAverStarPoint().compareTo(s2.getAverStarPoint()) == 0) {
            if(s1.getRevAmount() == s2.getRevAmount()) {
                if(s1.getRestaurantID() > s2.getRestaurantID()) {
                    ret = 1;
                } else if(s1.getRestaurantID() == s2.getRestaurantID()) {
                    ret = 0;
                } else if(s1.getRestaurantID() < s2.getRestaurantID()) {
                    ret = -1;
                }
            } else if(s1.getRevAmount() > s2.getRevAmount()) {
                ret = -1;
            } else if(s1.getRevAmount() < s2.getRevAmount()) {
                ret = 1;
            }
        }
        else if(s1.getAverStarPoint().compareTo(s2.getAverStarPoint()) < 0) {
            ret = 1;
        }
        return ret;
    }
}

/*
class NumberCompare implements Comparator<Restaurant> {
    int ret = 0;
    
    @Override
    public int compare(Restaurant s1, Restaurant s2) {
        if(s1.getRestaurantID() < s2.getRestaurantID()) 
            ret = -1;
        
        else if(s1.getRestaurantID() == s2.getRestaurantID()) 
            ret = 0;
        else if(s1.getRestaurantID() > s2.getRestaurantID()) 
            ret = 1;
        
        return ret;
    }
}
*/