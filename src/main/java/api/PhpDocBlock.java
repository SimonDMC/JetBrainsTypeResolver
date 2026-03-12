package api;

import java.util.List;
import java.util.Map;

public class PhpDocBlock {
    private final Map<String, List<DocTag>> tags;

    public PhpDocBlock(Map<String, List<DocTag>> tags) {
        this.tags = tags;
    }

    public List<DocTag> getTagsByName(String name) {
        return tags.get(name);
    }
}
