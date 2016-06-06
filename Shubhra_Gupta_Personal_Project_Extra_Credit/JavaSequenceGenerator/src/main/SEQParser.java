package main;

import dataModel.SeqAspect;

public class SEQParser {
    public static void main(String[] args) {
        new AppTest().runTest();
        String input = PlantSEQPreProcessor.getData(SeqAspect.getSeqDataList());
        new SEQGenerator().generate(input, "outSeq.png");
    }
}