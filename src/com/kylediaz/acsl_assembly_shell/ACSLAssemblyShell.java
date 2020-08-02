package com.kylediaz.acsl_assembly_shell;

import com.kylediaz.acsl_assembly_interpreter.CompilationException;
import com.kylediaz.acsl_assembly_interpreter.Compiler;
import com.kylediaz.acsl_assembly_interpreter.Interpreter;
import com.kylediaz.acsl_assembly_interpreter.statement.Statement;
import com.kylediaz.acsl_assembly_interpreter.statement.StatementLinkedListNode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ACSLAssemblyShell {

    private StatementLinkedListNode headNode;
    private StatementLinkedListNode tailNode;

    public void run() {
        printHelp();

        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String input = scanner.nextLine();
        while (!input.equals("EXIT")) {
            if (input.equals("HELP"))
                printHelp();
            else if (input.startsWith("LOADFILE ")) {
                String filePath = null;
                try {
                    filePath = input.substring("LOADFILE ".length());
                    FileReader fileReader = new FileReader(filePath);
                    append(fileReader);
                } catch (FileNotFoundException e) {
                    System.err.println("Unable to open " + filePath == null ? "" : filePath);
                }
            } else if (input.startsWith("LISTCODE"))
                listCode();
            else if (input.startsWith("RUN"))
                runCode();
            else if (input.startsWith("CLEAR"))
                clearQueuedCode();
            else {
                try {
                    append(input);
                } catch (Exception e) {
                    System.err.println("Compilation Error");
                    System.err.println(e.getMessage());
                }
            }

            input = scanner.nextLine();
        }
    }

    private Interpreter interpreter = new Interpreter();

    private void runCode() {
        if (headNode != null) {
            interpreter.run(headNode);
        }
    }

    /**
     * Attempts to append code from FileReader to the Statement linked list. If there is an error it will not
     * make any changes to the Statement linked list.
     */
    private void append(FileReader code) {
        BufferedReader reader = new BufferedReader(code);
        StatementLinkedListNode head = null, tail = null;
        if (reader != null) {
            try {
                List<String> lines = reader.lines().collect(Collectors.toList());
                for (String line : lines) {
                    Statement statement = Compiler.translate(line);
                    StatementLinkedListNode newNode = new StatementLinkedListNode(statement);

                    if (head == null)
                        head = newNode;
                    if (tail != null)
                        tail.setNext(newNode);
                    tail = newNode;
                }
            } catch (CompilationException e) {
                System.err.println(e);
            }
            if (this.headNode == null) {
                this.headNode = head;
                this.tailNode = tail;
            } else {
                this.tailNode.setNext(head);
                this.tailNode = tail;
            }

        }
    }
    private void append(String statement) {
        Statement compiledStatement = null;
        try {
            compiledStatement = Compiler.translate(statement);
        } catch (CompilationException ce) {
            System.err.println(ce.getMessage());
        }
        StatementLinkedListNode newNode = new StatementLinkedListNode(compiledStatement);
        if (headNode == null) {
            headNode = newNode;
        } else {
            tailNode.setNext(newNode);
        }
        tailNode = newNode;
    }

    private void printHelp() {
        System.out.println("ACSL Assembly Language Shell");
        System.out.println("Commands:");
        System.out.println("HELP - See this message again");
        System.out.println("LOADFILE $FILE - loads a file");
        System.out.println("Alternatively you can type in each command one at a time here");
        System.out.println("LISTCODE - Will list all loaded code");
        System.out.println("RUN - Will run all loaded code");
        System.out.println("CLEAR - Will erase all loaded code");
        System.out.println("EXIT - quits ASMCSL shell");
    }

    private void listCode() {
        if (headNode != null)
            System.out.println(headNode.buildCodeString());
    }

    private void clearQueuedCode() {
        headNode = null;
        tailNode = null;
    }

}
