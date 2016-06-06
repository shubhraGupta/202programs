package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class ParserRunner {
	private static Set<String> setOfFileNames = new HashSet<String>();
	
	public void parseData(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		
		for(File file : files){
			if (file.getName().endsWith("java")) {
				setOfFileNames.add(file.getName().split("\\.")[0]);
			}
		}
		
		for (File file : files) {
			try {
				if (file.getName().endsWith("java")) {
					FileInputStream in = new FileInputStream(file.getAbsolutePath());
					CodeParser
					.getInstance()
					.createCompilationUnitAction(in)
					.parseCompilationUnitAction();
				}
			} catch (FileNotFoundException e) {
				System.out.println("Files not found in the path specified");
				//e.printStackTrace();
			}
		}
	}
	
	public static Set<String> getSetOfFileNames(){
		return setOfFileNames;
	}
}
