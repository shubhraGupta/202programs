package main;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.SourceStringReader;

public class UMLGenerator {
	void generate(String source, String fileName) {
		try {
            OutputStream file = new FileOutputStream(fileName);
            SourceStringReader reader = new SourceStringReader(source);
            reader.generateImage(file);
        } catch (IOException e) {
        	System.out.println("Error in generating output file for uml diagram");
            e.printStackTrace();
        }
	}
}