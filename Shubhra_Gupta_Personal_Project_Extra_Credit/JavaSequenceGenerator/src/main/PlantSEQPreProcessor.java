package main;

import java.util.Arrays;
import java.util.List;

import dataModel.SeqData;
import dataModel.SeqType;

public class PlantSEQPreProcessor {
	public static String getData(List<SeqData> seqDataList) {
		StringBuilder source = new StringBuilder();
		source.append("@startuml");
		source.append("\n");
        for (SeqData seqData : seqDataList) {
        	if(seqData.getSeqType() == SeqType.CALL) {
        		String entryData = seqData.getSourceName() 
        				+ " -> " + seqData.getTargetName() 
        				+ " : " + seqData.getSignatureName() 
        				+ "(" + Arrays.deepToString(seqData.getArgs()) 
        				+ ")";
        		source.append(entryData + "\n");
        	}    
        	else if(seqData.getSeqType() == SeqType.EXIT) {
        		String exitData = seqData.getTargetName() 
        				+ " -> " + seqData.getSourceName() 
        				+ " : return" 
        				+ "(" 
        				+ Arrays.deepToString(seqData.getArgs()) 
        				+ ")";
                source.append(exitData + "\n");
        	}
        	else if (seqData.getSeqType() == SeqType.EXCEPTION){
                 source.append(seqData.getTargetName() 
                		 + " -> " + seqData.getSourceName() 
                		 + " : <color:red>throws" 
                		 + "(" 
                		 + Arrays.deepToString(seqData.getArgs()) 
                		 + ")</color>" 
                		 + "\n");
            }
        }
        source.append("@enduml");
        source.append("\n");
        return source.toString();
	}
}
