package com.kylediaz.acsl_assembly_interpreter.statement;

public class Statement {

    private String label;
    private Opcode opCode;
    private String LOC;

    public Statement(String label, Opcode opCode, String LOC) {
        this.label = label;
        this.opCode = opCode;
        this.LOC = LOC;
    }

    public String getLabel() {
        return label;
    }
    public Opcode getOpCode() {
        return opCode;
    }
    public String getLOC() {
        return LOC;
    }

    public boolean LOCIsLiteral() {
        return LOC.charAt(0) == '=';
    }

    @Override
    public String toString() {
        String output = null;
        if (label != null && label.isBlank()) {
            output = String.format("%s %s", opCode.toString(), LOC);
        } else {
            output = String.format("%s %s %s", label, opCode.toString(), LOC);
        }
        return output;
    }

}
