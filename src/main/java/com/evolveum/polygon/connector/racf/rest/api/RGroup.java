package com.evolveum.polygon.connector.racf.rest.api;

import java.util.ArrayList;
import java.util.List;

public class RGroup extends RObject{

    private String description;
    private List<String> members;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<String> getMembers() {
        if (members == null) {
            members = new ArrayList<>();
        }

        return members;
    }

    public void setMembers(List<String> members) { this.members = members; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RGroup rGroup = (RGroup) o;

        if (description != null ? !description.equals(rGroup.description) : rGroup.description != null) return false;
        return members != null ? members.equals(rGroup.members) : rGroup.members == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RGroup{");
        sb.append("description='").append(description).append("'");
        sb.append(", members").append(members);
        sb.append("}");
        return sb.toString();
    }
}
