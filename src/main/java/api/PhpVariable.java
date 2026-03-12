package api;

public class PhpVariable {
    private final PhpDocBlock docBlock;
    private final String name;

    public PhpVariable(PhpDocBlock docBlock, String name) {
        this.docBlock = docBlock;
        this.name = name;
    }

    public PhpDocBlock getDocBlock() {
        return docBlock;
    }

    public String getName() {
        return name;
    }
}
