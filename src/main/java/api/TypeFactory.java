package api;

import java.util.ArrayList;
import java.util.List;

public class TypeFactory {
    public PhpType createType(String typeName) {
        return new SingleType(typeName);
    }

    public UnionType createUnionType(List<PhpType> types) {
        List<SingleType> typeUnion = new ArrayList<>();

        for (PhpType type : types) {
            // if it's a single type, add directly
            if (type instanceof SingleType singleType) {
                typeUnion.add(singleType);
            }
            
            // if it's a union type, add all containing types
            if (type instanceof UnionType unionType) {
                typeUnion.addAll(unionType.getTypes());
            }
        }

        return new UnionType(typeUnion);
    }
}
