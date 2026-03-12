package api;

import java.util.List;

public class UnionType implements PhpType {
    private final List<SingleType> types;

    public UnionType(List<SingleType> types) {
        this.types = types;
    }

    public List<SingleType> getTypes() {
        return types;
    }
}
