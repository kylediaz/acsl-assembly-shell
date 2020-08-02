package com.kylediaz.acsl_assembly_interpreter.statement;

public class StatementLinkedListNode {

    private StatementLinkedListNode next;
    private Statement statement;

    public StatementLinkedListNode(Statement statement) {
        this.statement = statement;
    }
    public StatementLinkedListNode(Statement statement, StatementLinkedListNode next) {
        this(statement);
        this.next = next;
    }

    public StatementLinkedListNode getNext() {
        return next;
    }
    public void setNext(StatementLinkedListNode next) {
        this.next = next;
    }

    public Statement getStatement() {
        return statement;
    }

    public String buildCodeString() {
        StringBuilder builder = new StringBuilder();
        buildCodeString(builder);
        return builder.toString();
    }
    private void buildCodeString(StringBuilder builder) {
        builder.append(statement.toString());
        builder.append('\n');
        if (next != null)
            next.buildCodeString(builder);
    }

    @Override
    public String toString() {
        return "Node for " + statement;
    }
}
