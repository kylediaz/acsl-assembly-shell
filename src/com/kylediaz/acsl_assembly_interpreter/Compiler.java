package com.kylediaz.acsl_assembly_interpreter;

import com.kylediaz.acsl_assembly_interpreter.statement.Opcode;
import com.kylediaz.acsl_assembly_interpreter.statement.Statement;

import java.util.ArrayList;

public class Compiler {

    public static Statement translate(String statement) throws CompilationException {
        String[] args = statement.split(" ");
        String label = null, opcode= null, LOC = null;
        if (args.length == 1) {
            label = "";
            opcode = args[0];
            LOC = "";
        } else if (args.length == 2) {
            /*
            When there are two arguments, then the statement can be in the format LABEL OPCODE or OPCODE LOC
            I distinguish between the two by seeing if the valid Opcode is the first or second arg
             */
            boolean arg0IsOpcode = isValidOpcode(args[0]);
            boolean arg1IsOpcode = isValidOpcode(args[1]);
            if (arg0IsOpcode && arg1IsOpcode) {
                throw new CompilationException("Invalid use of keyword");
            } else if (arg0IsOpcode) {
                label = "";
                opcode = args[0];
                LOC = args[1];
            } else if (arg1IsOpcode) {
                label = args[0];
                opcode = args[1];
                LOC = "";
            } else {
                throw new CompilationException("Unable to find valid opcode");
            }
        } else if (args.length == 3) {
            label = args[0];
            opcode = args[1];
            LOC = args[2];
        } else {
            throw new CompilationException("Unexpected number of args: " + args.length);
        }

        Opcode parsedOpcode = safeParseForOpcode(opcode);

        String problems = verifyRequiredParametersArePresent(label, parsedOpcode, LOC);
        if (problems != null) {
            throw new CompilationException(problems);
        }

        Statement output = new Statement(label, parsedOpcode, LOC);
        return output;
    }

    private static Opcode safeParseForOpcode(String opcode) throws CompilationException {
        Opcode parsedOpcode;
        try {
            parsedOpcode = Opcode.valueOf(opcode.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CompilationException("Unexpected opcode " + opcode);
        } catch (Exception e) {
            throw new CompilationException("Unknown error");
        }
        return parsedOpcode;
    }

    private static boolean isValidOpcode(String s) {
        for (Opcode opcode : Opcode.values()) {
            if (opcode.toString().equals(s))
                return true;
        }
        return false;
    }

    /**
     * @return String of all errors, null if no problems are present
     */
    private static String verifyRequiredParametersArePresent(String label, Opcode opcode, String LOC) {
        ArrayList<String> errors = null;
        if (opcode.labelIsRequired() && label.isBlank()) {
            if (errors == null)
                errors = new ArrayList<>();
            errors.add("Label is required but none provided");
        }
        if (opcode.LOCIsRequired() && LOC.isBlank()) {
            if (errors == null)
                errors = new ArrayList<>();
            errors.add("LOC is required but none provided");
        }

        String output = null;
        if (errors != null && errors.size() > 0) {
            output = String.join("\n", errors);
        }
        return output;
    }

}
