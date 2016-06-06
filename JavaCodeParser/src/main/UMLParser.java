package main;
public class UMLParser {

	public static void main(String[] args) {
			if(args.length < 2){
				System.out.println("not proper arguments");
				System.out.println("Usage : java -jar umlparser.jar <classPath> <output image file name>");
				return;
			}
			try{
				new ParserRunner().parseData(args[0]);
				String input = PlantUMLPreProcessor.getData(CodeParser.getClassDataList());
				System.out.println(input);
				new UMLGenerator().generate(input,args[1]);
			}catch(Exception e){
				System.out.println("Not proper input data to parse or generate uml");
				//e.printStackTrace();
			}
	}
}
