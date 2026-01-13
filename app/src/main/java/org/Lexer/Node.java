package org.Lexer;

import java.util.ArrayList;
import java.util.List;

public final class Node {
    private final String data;
    private List<Node> children;

    public Node(final String data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public  String getData() {
        return data;
    }

    public void setChild(final Node child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(final int level) {
        final StringBuilder result = new StringBuilder();
        final String indent = "\t".repeat(level);

        result.append(indent)
                .append("Node(")
                .append(data)
                .append(")\n");

        if (!children.isEmpty()) {
            result.append(indent)
                    .append("Children: \n");

            for (Node child : children) {
                result.append(child.toString(level + 1));
            }
        }

        return result.toString();
    }
}
