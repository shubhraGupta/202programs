package dataModel;
public enum ClassType {
	CLASS("class"),
	INTERFACE("interface");
	
	private String classType;
	
	ClassType(String classType){
		this.classType = classType;
	}
	
	public String getClassType(){
		return this.classType;
	}
}
