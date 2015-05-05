package zunair.syed.trackyourones.common;

import android.graphics.Bitmap;

public class Person {

	private String name;
	private String status;
	private String location;
	private Bitmap picture;
	
	public Person(String name, String status, String location )
	{
		this.name = name;
		this.status = status;
		this.location = location;
	}

	
	public String getName()
	{
		return this.name;
	}
	
	public String getStatus()
	{
		return this.status;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public Bitmap getPicture()
	{
		return this.picture;
	}
	
	public void setPicture(Bitmap picture)
	{
		this.picture = picture;
	}
	
	
}
