package com.kylediaz.acsl_assembly_interpreter;

import com.kylediaz.acsl_assembly_interpreter.statement.Opcode;
import com.kylediaz.acsl_assembly_interpreter.statement.Statement;
import com.kylediaz.acsl_assembly_interpreter.statement.StatementLinkedListNode;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {

    private final static int MODULO = 1_000_000;

    private StatementLinkedListNode currentNode;

    private int ACC;
    private Map<String, Integer> variables = new HashMap<>();
    private Map<String, StatementLinkedListNode> labelPointers = new HashMap<>();

    private InputStream inputStream;
    private Scanner inputScanner;
    private PrintStream printStream;

    public Interpreter(InputStream inputStream, PrintStream printStream) {
        this.inputStream = inputStream;
        inputScanner = new Scanner(inputStream);
        this.printStream = printStream;
    }
    public Interpreter() {
        this(System.in, System.out);
    }

    public void run(Statement statement) {
        run(new StatementLinkedListNode(statement));
    }

    public void run(StatementLinkedListNode startingNode) {
        findAllLabelPointers(startingNode);
        currentNode = startingNode;
        while (currentNode != null && currentNode.getStatement().getOpCode() != Opcode.END) {
            Statement statement = currentNode.getStatement();
            try {
                execute(statement);
            } catch (Exception e) {
                String msg = String.format("Error on line\n> %s\n%s", statement.toString(), e.getMessage());
                System.err.println(msg);
                return;
            }
            currentNode = currentNode.getNext();
        }
    }

    private void execute(Statement statement) {
        switch (statement.getOpCode()) {
            // Real Opcodes
            case LOAD -> ACC = evaluateLOC(statement.getLOC());
            case STORE -> variables.put(statement.getLOC(), ACC);
            case ADD -> ACC += evaluateLOC(statement.getLOC());
            case SUB -> ACC -= evaluateLOC(statement.getLOC());
            case MULT -> ACC *= evaluateLOC(statement.getLOC());
            case DIV -> ACC /= evaluateLOC(statement.getLOC());
            case BG -> {
                if (ACC > 0)
                    branchTo(statement.getLOC());
            }
            case BE -> {
                if (ACC == 0)
                    branchTo(statement.getLOC());
            }
            case BL -> {
                if (ACC < 0)
                    branchTo(statement.getLOC());
            }
            case BU -> branchTo(statement.getLOC());
            case READ -> {
                if (printStream == System.out && inputStream == System.in) {
                    System.out.print(statement.getLOC() + " = ");
                }
                try {
                    String input = inputScanner.next();
                    int parsedInput = Integer.parseInt(input);
                    variables.put(statement.getLOC(), parsedInput);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid input - expected integer");
                }
            }
            case PRINT -> printStream.println(evaluateLOC(statement.getLOC()));
            case DC -> variables.put(statement.getLabel(), evaluateLOC(statement.getLOC()));
            case END -> {
                return;
            }
            // Custom Opcodes
            case PRINTVARS -> printStream.println(variables);
            default -> throw new RuntimeException("Unexpected Opcode: " + statement.getOpCode());
        }
        ACC %= MODULO;
    }

    /**
     * Clears all code, variables, and labels
     */
    private void resetEnvironment() {
        ACC = 0;
        variables.clear();
        labelPointers.clear();
    }

    private void findAllLabelPointers(StatementLinkedListNode startingNode) {
        currentNode = startingNode;
        while (currentNode != null) {
            Statement currentStatement = currentNode.getStatement();
            String label = currentStatement.getLabel();
            if (!label.isBlank()) {
                labelPointers.put(label, currentNode);
            }
            currentNode = currentNode.getNext();
        }
    }

    private Integer evaluateLOC(String LOCValue) {
        Integer output = null;
        if (isValidLiteralValue(LOCValue)) {
            output = parseLiteralValue(LOCValue);
        } else if (variables.containsKey(LOCValue)) {
            output = variables.get(LOCValue);
        }
        if (output == null) {
            throw new RuntimeException("Invalid LOC Value");
        }
        return output;
    }
    private static boolean isValidLiteralValue(String value) {
        return value.matches("=?-?\\d+");
    }
    /**
     * @param literalString in the format `=\d+`
     * @return Integer on successful parse, null otherwise
     */
    private static Integer parseLiteralValue(String literalString) {
        Integer output;
        try {
            if (literalString.charAt(0) == '=')
                literalString = literalString.substring(1);
            output = Integer.parseInt(literalString);
        } catch (NumberFormatException e) {
            output = null;
        }
        return output;
    }

    private void branchTo(String label) {
        if (labelPointers.containsKey(label)) {
            currentNode = labelPointers.get(label);
            execute(currentNode.getStatement());
        } else {
            throw new RuntimeException("No label " + label + " exists");
        }
    }

}
