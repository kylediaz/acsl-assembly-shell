package com.kylediaz.acsl_assembly_interpreter.statement;

public enum Opcode {
    // Real Opcodes
    LOAD(false, true),
    STORE(false, true),
    ADD(false, true),
    SUB(false, true),
    MULT(false, true),
    DIV(false, true),
    BG(false, true),
    BE(false, true),
    BL(false, true),
    BU(false, true),
    READ(false, true),
    PRINT(false, true),
    DC(true, true),
    END(false, false),

    // Custom Opcodes
    PRINTVARS(false, false)
    ;
    private final boolean labelRequired;
    private final boolean LOCRequired;
    private Opcode(boolean labelRequired, boolean LOCRequired) {
        this.labelRequired = labelRequired;
        this.LOCRequired = LOCRequired;
    }

    public boolean labelIsRequired() {
        return labelRequired;
    }

    public boolean LOCIsRequired() {
        return LOCRequired;
    }
}
