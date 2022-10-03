package tableClass;

import java.io.Serializable;

//������ ���̺�
public class Restaurant implements Serializable
{
	private static final long serialVersionUID = 4L;
	
	private int restaurantID;				//������ ID
	private String restaurantName;			//���Ҹ�
	private String address;					//�ּ�
	private String district;				//��������
	private String contactAddress;			//����ó
	private String mainDish;				//�� �޴�
	private Double averStarPoint;			//��պ���
	
	private int revAmount;					//���� ����(�ӽ� ����)
	
	public Restaurant()
	{
		restaurantID = 0; restaurantName =null; address =null; district = null; contactAddress =null;
		mainDish = null; averStarPoint = 0.0; revAmount = 0;
	}

	public Restaurant(int restaurantID, String restaurantName, String address, String district,
			String contactAddress, String mainDish)
	{
		this.restaurantID = restaurantID; this.restaurantName =restaurantName; this.address =address;
		this.district = district; this.contactAddress =contactAddress; this.mainDish = mainDish;
		this.averStarPoint = 0.0; revAmount = 0;
	}
	
	public int getRestaurantID() {return restaurantID;}
	public String getRestaurantName() {return restaurantName;}
	public String getAddress() {return address;}
	public String getDistrict() {return district;}
	public String getContactAddress() {return contactAddress;}
	public String getMainDish() {return mainDish;}
	public Double getAverStarPoint() {return averStarPoint;}
	public int getRevAmount() {return revAmount;}
	
	public void setRestaurantID(int restaurantID) {this.restaurantID = restaurantID;}
	public void setRestaurantName(String restaurantName) {this.restaurantName =restaurantName;}
	public void setAddress(String address) {this.address =address;}
	public void setDistrict(String district) {this.district = district;}
	public void setContactAddress(String contactAddress) {this.contactAddress =contactAddress;}
	public void setMainDish(String mainDish) {this.mainDish = mainDish;}
	public void setAverStarPoint(Double averStarPoint) {this.averStarPoint = averStarPoint;}
	public void setRevAmount(int revAmount) {this.revAmount = revAmount;}
}