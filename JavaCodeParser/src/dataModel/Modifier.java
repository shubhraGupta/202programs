package dataModel;
public enum Modifier {
	DEFAULT(0, "", '-'),
	PUBLIC(1, "public", '+'),
	PRIVATE(2, "private", '-'),
	PROTECTED(4, "protected", '#');
	
	private final int modNum;
    private final String modStr;
    private final char modSym;
	
	Modifier(int modNum, String modStr, char modSym){
		this.modNum = modNum;
		this.modStr = modStr;
		this.modSym = modSym;
	}
	
	public int getModNum(){
		return modNum;
	}
	
	public String getModStr() {
        return modStr;
    }

    public char getModSym(){
        return modSym;
    }
}
