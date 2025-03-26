package sk.edenis.taskmanager.model;


public enum ClientType {
	
	ADMIN("admin"),
	USER("user");
	
	private final String displayType;
	
	ClientType(String displayType){
		this.displayType = displayType;
	}
	
	public String getDisplayType() {
		return displayType;
	}
}
