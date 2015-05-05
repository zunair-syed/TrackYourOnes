package zunair.syed.trackyourones.common;

public class Message {

	private String contentMessage;
	private String CompositionDate;
	private String posterName;
	
	public Message(String contentMessage, String CompositionDate, String posterName )
	{
		this.contentMessage = contentMessage;
		this.CompositionDate = CompositionDate;
		this.posterName = posterName;
	}

	
	public String getContentMessage()
	{
		return this.contentMessage;
	}
	
	public String getCompositionDate()
	{
		return this.CompositionDate;
	}
	
	public String getPosterName()
	{
		return this.posterName;
	}
	
	
	
}
