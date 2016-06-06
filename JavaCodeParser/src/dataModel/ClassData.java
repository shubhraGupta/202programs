package dataModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassData {
	
	private String name;
	private int modifier;
	private ClassType classType;
	private List<String> interfaceImpls = new ArrayList<String>();
	private List<String> classExtends = new ArrayList<String>();
	private Set<String> usesClasses = new HashSet<String>();
	private List<MethodData> constructors = new ArrayList<MethodData>();
	private List<VariableData> variables = new ArrayList<VariableData>();
	private List<VariableData> classVariables = new ArrayList<VariableData>();
	private List<MethodData> methods = new ArrayList<MethodData>();
	
	public static List<String> relations = new ArrayList<String>();
	
	//public static Set<String>  classNameSet = new HashSet<String>();
	
	
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
	
	public ClassType getClassType(){
		return this.classType;
	}
	
	public void setClassType(ClassType classType){
		this.classType = classType;
	}
	
	public List<String> getInterfaceImpls(){
		return this.interfaceImpls;
	}
	
	public void setInterfaceImpls(List<String> interfaceImpls){
		this.interfaceImpls = interfaceImpls;
	}
	
	public List<String> getClassExtends(){
		return this.classExtends;
	}
	
	public void setClassExtends(List<String> classExtends){
		this.classExtends = classExtends;
	}
	
	public Set<String> getUsesClasses(){
		return this.usesClasses;
	}
	
	public void setUsesClasses(Set<String> usesClasses){
		this.usesClasses = usesClasses;
	}
	
	public List<VariableData> getVariables(){
		return this.variables;
	}
	
	public void setVariables(List<VariableData> variables){
		this.variables = variables;
	}
	
	public List<VariableData> getClassVariables(){
		return this.classVariables;
	}
	
	public void setClassVariables(List<VariableData> classVariables){
		this.classVariables = classVariables;
	}
	
	public List<MethodData> getMethods(){
		return this.methods;
	}
	
	public void setMethods(List<MethodData> methods){
		this.methods = methods;
	}
	
	public List<MethodData> getConstructors(){
		return this.constructors;
	}
	
	public void setConstructors(List<MethodData> constructors){
		this.constructors = constructors;
	}
}
