package dataModel;
import java.util.ArrayList;
import java.util.List;

public class MethodData {
	private String name;
	private int modifier;
	private String returnType;
	private List<VariableData> args = new ArrayList<VariableData>();
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getModifier(){
		return this.modifier;
	}
	
	public void setModifier(int modifier){
		this.modifier = modifier;
	}
	
	public String getReturnType(){
		return this.returnType;
	}
	
	public void setReturnType(String returnType){
		this.returnType = returnType;
	}
	
	public List<VariableData> getArgs(){
		return this.args;
	}
	
	public void setArgs(List<VariableData> args){
		this.args = args;
	}
}
