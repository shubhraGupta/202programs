package dataModel;
public class VariableData {
	private String name;
	private String type;
	private int modifier;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public int getModifier(){
		return this.modifier;
	}
	
	public void setModifier(int modifier){
		this.modifier = modifier;
	}
}
