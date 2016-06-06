package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataModel.ClassData;
import dataModel.ClassType;
import dataModel.MethodData;
import dataModel.VariableData;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import japa.parser.JavaParser;
import japa.parser.ParseException;

public class CodeParser {
	private CompilationUnit cu = null;
	private ClassData classData = null;
	private static List<ClassData> classDataList= new ArrayList<ClassData>();
	private static CodeParser s_instance = null;
	
	private CodeParser(){
		
	}
	
	public static CodeParser getInstance(){
		if(s_instance == null)
			return new CodeParser();
		return s_instance;
	}
	
	public CodeParser createCompilationUnitAction(FileInputStream in) {
        try
        {
                cu = JavaParser.parse(in);
        }
        catch(ParseException x)
        {
             System.out.println("Error in creating compilation units");
        }
        finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Error in parsing java files");
				//e.printStackTrace();
			}
		}
        return this;
	}
	
	public CodeParser parseCompilationUnitAction(){
		classData = new ClassData();
		
		updateClassOrInterface(cu.getTypes(), classData);
		
		
		final List<MethodData> constructorDataList = new ArrayList<MethodData>();
		final List<MethodData> methodDataList = new ArrayList<MethodData>();
		final List<VariableData> varDataList = new ArrayList<VariableData>();
		final Set<String> usesClassList = new HashSet<String>();
		
		new VoidVisitorAdapter<Object>() {
			
			@Override
			public void visit(ClassOrInterfaceDeclaration node, Object arg) {
				/*
				 * * For class and interface it extends and implements respectively
				 */
				classData.setName(node.getName());
				List<ClassOrInterfaceType> listExtends = node.getExtends();
				if(listExtends != null){
					List<String> classExtends = new ArrayList<String>();
					for(ClassOrInterfaceType e : listExtends){
						classExtends.add(e.toString());
					}
					
					classData.setClassExtends(classExtends);
				}
				else
				{
					classData.setClassExtends(null);
				}
				
				List<ClassOrInterfaceType> listImplements = node.getImplements();
				if(listImplements != null){
					List<String> interfaceImpls = new ArrayList<String>();
					for(ClassOrInterfaceType e : listImplements){
						interfaceImpls.add(e.toString());
					}
					
					classData.setInterfaceImpls(interfaceImpls);
				}
				else
				{
					classData.setInterfaceImpls(null);
				}
				super.visit(node, arg);
			}

			@Override
			public void visit(FieldDeclaration node, Object arg) {
				/*
				 * For variables it has
				 */
				String nodeType = node.getType().toString();
				if(node.getType() instanceof PrimitiveType || nodeType.contains("String") || nodeType.contains("[]")){
					//regular field
					VariableData vd = new VariableData();
					vd.setName(node.getVariables().get(0).toString());
					vd.setModifier(node.getModifiers());
					vd.setType(nodeType);
					varDataList.add(vd);
				}
				else if(nodeType.contains("Collection")){
					//many to one relation
					ClassData.relations.add(classData.getName() + "\"1\"" + " -- " + "\"*\"" + getDataTypeName(nodeType));
				}
				else{
					//one to one relation
					boolean doAdd = true;;
					for(String rel : ClassData.relations){
						//one to one or one to many already exist then do not add any relation
						if(rel.contains(classData.getName().toString()) && rel.contains(nodeType))
							doAdd = false;
					}
					if(doAdd){
						ClassData.relations.add(classData.getName() + "\"1\"" + " -- " + "\"1\"" + nodeType);
					}
				}
				
				super.visit(node, arg);
			}
			
			@Override
			public void visit(ConstructorDeclaration node, Object arg) {
				/*
				 * For constructors it has
				 */
				MethodData md = new MethodData();
				md.setName(node.getName());
				md.setModifier(node.getModifiers());
				md.setReturnType(null);
				List<Parameter> allParams = node.getParameters();
				if(allParams != null){
					List<VariableData> varDataList = new ArrayList<VariableData>();
					for(Parameter params : allParams){
						VariableData vd = new VariableData();
						String paramData[] = params.toString().split(" ");
						if(params.getType() instanceof PrimitiveType || paramData[0].contains("String") || paramData[0].contains("[]")){
							//do nothing
						}
						else{
							usesClassList.add(params.getType().toString());
						}
						vd.setType(paramData[0]);
						vd.setName(paramData[1]);
						varDataList.add(vd);
					}
					md.setArgs(varDataList);
				}
				constructorDataList.add(md);
			}

			@Override
			public void visit(MethodDeclaration node, Object arg) {
				/*
				 * For methods it has
				 */
				MethodData md = new MethodData();
				md.setName(node.getName());
				md.setModifier(node.getModifiers());
				md.setReturnType(node.getType().toString());
				if(node.getBody() != null){
					if(node.getBody().getStmts() != null){
						for(int i=0; i<node.getBody().getStmts().size(); i++){
							String bodyObjectType = getFirstWordInString(node.getBody().getStmts().get(i).toString());
							if(ParserRunner.getSetOfFileNames().contains(bodyObjectType)){
								usesClassList.add(bodyObjectType);
							}
						}
					}
				}
				
				List<Parameter> allParams = node.getParameters();
				if(allParams != null){
					List<VariableData> varDataList = new ArrayList<VariableData>();
					for(Parameter params : allParams){
						VariableData vd = new VariableData();
						String paramData[] = params.toString().split(" ");
						if(params.getType() instanceof PrimitiveType || paramData[0].contains("String") || paramData[0].contains("[]")){
							//do nothing
						}
						else{
							usesClassList.add(params.getType().toString());
						}
						vd.setType(paramData[0]);
						vd.setName(paramData[1]);
						varDataList.add(vd);
					}
					md.setArgs(varDataList);
				}
				//System.out.println(md.getName());
				methodDataList.add(md);
				super.visit(node, arg);
			}
			
		}.visit(cu, null);
		
		classData.setUsesClasses(usesClassList);
		classData.setConstructors(constructorDataList);
		classData.setMethods(methodDataList);
		classData.setVariables(varDataList);
		classDataList.add(classData);
		
		return this;
	}
	
	public void updateClassOrInterface(List<TypeDeclaration> types, ClassData classData){
		for(TypeDeclaration type : types){
			//ClassData.classNameSet.add(type.getName().toString());
			
			if(type.toString().contains(ClassType.CLASS.getClassType())){
				classData.setClassType(ClassType.CLASS);
			}
			else if(type.toString().contains(ClassType.INTERFACE.getClassType())){
				classData.setClassType(ClassType.INTERFACE);
			}
			else{
				classData.setClassType(null);
			}
		}
	}
	
	public String getDataTypeName(String str){
		try{
			String result = str.substring(str.indexOf("<") + 1, str.indexOf(">"));
			return result;
		}
		catch(StringIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public String getFirstWordInString(String str){
		try{
			String result = str.substring(0, str.indexOf(" "));
			return result;
		}
		catch(StringIndexOutOfBoundsException e){
			return str;
		}
	}
	
	public static List<ClassData> getClassDataList(){
		return classDataList;
	}
	
}
