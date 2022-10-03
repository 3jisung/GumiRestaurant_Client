package tableClass;

import java.io.Serializable;

//���� ���̺�
public class Review implements Serializable
{
	private static final long serialVersionUID = 3L;
	
	private int number;					//��üŰ
	private String userID;				//����� ID
	private int restaurantID;			//������ ID
	private Double starPoint;			//����
	private String content;				//���䳻��
	
	public Review()
	{
		number = 0; userID =null; restaurantID =0; starPoint = 0.0; content =null;
	}

	public Review(String userID, int restaurantID, Double starPoint, String content)
	{
		this.userID= userID; this.restaurantID = restaurantID;
		this.starPoint =starPoint; this.content =content;
		number = 0;
	}
	
	public int getNumber() {return number;}
	public String getUserID() {return userID;}
	public int getRestaurantID() {return restaurantID;}
	public Double getStarPoint() {return starPoint;}
	public String getContent() {return content;}
	
	public void setNumber(int number) {this.number = number;}
	public void setUserID(String userID) {this.userID = userID;}
	public void setRestaurantID(int restaurantID) {this.restaurantID = restaurantID;}
	public void setStarPoint(Double starPoint) {this.starPoint = starPoint;}
	public void setContent(String content) {this.content = content;} 
}