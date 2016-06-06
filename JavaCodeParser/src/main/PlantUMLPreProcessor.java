package main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataModel.ClassData;
import dataModel.ClassType;
import dataModel.MethodData;
import dataModel.VariableData;

public class PlantUMLPreProcessor {
	public static String getData(List<ClassData> classDataList){
		
		improveClassData(classDataList);
		Set<String> interfaceSet = getInterfaceNameSet(classDataList);
		
		StringBuilder plantUMLInput = new StringBuilder();
		plantUMLInput.append("@startuml" + "\n");
		plantUMLInput.append("skinparam classAttributeIconSize 0 ");
		plantUMLInput.append("\n");
		for(ClassData classData : classDataList){
			
			if(classData.getInterfaceImpls() != null){
				for(String interfaceImpl : classData.getInterfaceImpls()){
					plantUMLInput.append(interfaceImpl + " <|.. " + classData.getName() + "\n");
				}
			}
			
			if(classData.getClassExtends() != null){
				for(String classExt : classData.getClassExtends()){
					plantUMLInput.append(classExt + " <|-- " + classData.getName() + "\n");
				}
			}
			
			if (classData.getClassType().equals(ClassType.CLASS))
				plantUMLInput.append("class " + classData.getName() + " {\n");
			else if(classData.getClassType().equals(ClassType.INTERFACE))
				plantUMLInput.append("interface " + classData.getName() + " {\n");
			
			if(!classData.getVariables().isEmpty()){
				for(VariableData varData : classData.getVariables()){
					if(varData.getModifier() != 1 && varData.getModifier() !=2)
						continue;
					plantUMLInput.append(getHelperModifier(varData.getModifier()));
					plantUMLInput.append(varData.getName() + " : " + varData.getType() + "\n");
				}
			}
			
			if(!classData.getConstructors().isEmpty()){
				for(MethodData methodData : classData.getConstructors()){
					plantUMLInput.append(getHelperModifier(methodData.getModifier()) + " ");
					plantUMLInput.append(methodData.getName() + "(");
					for(VariableData arg : methodData.getArgs()){
						plantUMLInput.append(arg.getName() + " : " + arg.getType() + ",");
					}
					//deleting extra comma
					if(plantUMLInput.charAt(plantUMLInput.length()-1) == ','){
						plantUMLInput.deleteCharAt(plantUMLInput.length()-1);
					}
					plantUMLInput.append(")" + "\n");
				}
				plantUMLInput.append("\n");
			}
			
			if(!classData.getMethods().isEmpty()){
				for(MethodData methodData : classData.getMethods()){
					//if(methodData.getModifier() != 1 && methodData.getModifier() !=2)
						//continue;
					
					if(methodData.getModifier() != 2){
						/*
						 * not including private methods
						 */
						plantUMLInput.append(getHelperModifier(methodData.getModifier()) + " ");
						plantUMLInput.append(methodData.getName() + "(");
						for(VariableData arg : methodData.getArgs()){
							plantUMLInput.append(arg.getName() + " : " + arg.getType() + ",");
						}
						//deleting extra comma
						if(plantUMLInput.charAt(plantUMLInput.length()-1) == ','){
							plantUMLInput.deleteCharAt(plantUMLInput.length()-1);
						}
						plantUMLInput.append(")" + " : " + methodData.getReturnType() + "\n");
					}
				}
				plantUMLInput.append("\n");
			}
			
			
			plantUMLInput.append("}");
			plantUMLInput.append("\n");
			if(classData.getClassType() == ClassType.CLASS && classData.getUsesClasses() != null){
				for(String usesClass : classData.getUsesClasses()){
					if(interfaceSet.contains(usesClass)){
						plantUMLInput.append(classData.getName() + " ..> \"uses\" " + usesClass + "\n");
					}
				}
			}
		}
		
		
		if(!ClassData.relations.isEmpty()){
			for(String rel : ClassData.relations){
				plantUMLInput.append(rel + "\n");
			}
		}
		
		plantUMLInput.append("@enduml");
		
		return plantUMLInput.toString();
	}
	
	public static String getHelperModifier(int modifier){
		switch(modifier){
		case 0 : return "~";
		case 1 : return "+";
		case 2 : return "-";
		case 4 : return "#";
		default: return "+";
		}
	}
	
	public static void improveClassData(List<ClassData> classDataList){
		
		for(ClassData classData : classDataList){
		
			if(!classData.getVariables().isEmpty() && !classData.getMethods().isEmpty()){
				for(VariableData varData : classData.getVariables()){
					if(varData.getModifier() == 2){
						updateVarGetSet(varData, classData.getMethods());
					}
				}
			}
		}
	}
	
	public static void updateVarGetSet(VariableData varData, List<MethodData> methodDataList){
		String getVarName = "get" + varData.getName().substring(0,1).toUpperCase() 
						+ varData.getName().substring(1, varData.getName().length());
		
		String setVarName = "set" + varData.getName().substring(0,1).toUpperCase() 
				+ varData.getName().substring(1, varData.getName().length());
		
		//System.out.println("get var name : " +  getVarName);
		
		//System.out.println("set var name : " +  setVarName);

		
		boolean isGetPublic = false;
		boolean isSetPublic = false;
		
		int getIndex = -1;
		int setIndex = -1;
		int i = 0;
		
		for(MethodData methodData : methodDataList){
			if(methodData.getName().equals(getVarName) && methodData.getModifier() == 1){
				isGetPublic = true;
				getIndex = i;
			}
			
			if(methodData.getName().equals(setVarName) && methodData.getModifier() == 1){
				isSetPublic = true;
				setIndex = i;
			}
			
			i++;
		}
		
		if(isGetPublic && isSetPublic){
			varData.setModifier(1);
			methodDataList.remove(getIndex);
			methodDataList.remove(setIndex-1);
		}
	}
	
	public static Set<String> getInterfaceNameSet(List<ClassData> classDataList){
		Set<String> interfaceSet = new HashSet<String>();
		for(ClassData classData : classDataList){
			if(classData.getClassType() == ClassType.INTERFACE){
				interfaceSet.add(classData.getName());
			}
		}	
		return interfaceSet;
	}
}