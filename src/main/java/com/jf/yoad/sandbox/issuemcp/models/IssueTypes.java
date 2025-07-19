package com.jf.yoad.sandbox.issuemcp.models;

public enum IssueTypes {
    BUG(10014L),
    TASK(10021L);

    public final long id;

    private IssueTypes(long id) {
        this.id = id;
    }
}
