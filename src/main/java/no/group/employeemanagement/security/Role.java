package no.group.employeemanagement.security;

public enum Role {
    ADMIN,
    MANAGER,
    STAFF;

    public String getPrefixedName() {
        return "ROLE_" + this.name();
    }
}
