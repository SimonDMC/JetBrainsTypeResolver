package api;

public class SingleType implements PhpType {
    private final String typeName;

    public SingleType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
