import api.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeResolver {
    public PhpType inferTypeFromDoc(PhpVariable variable) {
        TypeFactory typeFactory = new TypeFactory();

        PhpDocBlock docBlock = variable.getDocBlock();

        // return mixed if no docblock
        if (docBlock == null) {
            return typeFactory.createType("mixed");
        }

        List<DocTag> docTags = docBlock.getTagsByName("@var");

        // return mixed if no @var annotations
        if (docTags == null || docTags.isEmpty()) {
            return typeFactory.createType("mixed");
        }

        // figure out if we're taking type annotation from named or anonymous tag
        int anonymousTags = 0;
        // variable name -> type map
        Map<String, String> annotationMap = new HashMap<>();

        for (DocTag docTag : docTags) {
            // this process assumes we're dealing with well-formed, valid tags.
            // e.g. if we get multiple anonymous tags, we process that as ambiguous,
            // but if tag values are complete gibberish, this will likely spit out
            // nonsense instead of detecting it

            String value = docTag.getValue();
            if (value.contains(" ")) {
                // named annotation
                String[] parts = value.split(" ");
                annotationMap.put(parts[1], parts[0]);
            } else {
                // anonymous annotation
                anonymousTags++;
            }
        }

        // figure out which tag we're getting the type from
        String unprocessedType;
        if (anonymousTags == 1 && docTags.size() == 1) {
            // if we only got one anonymous annotation, this is the type we're looking for
            unprocessedType = docTags.getFirst().getValue();
        } else if (annotationMap.containsKey(variable.getName())) {
            // or, if we have a named annotation matching the variable name, that's what we want
            unprocessedType = annotationMap.get(variable.getName());
        } else {
            // otherwise, it's ambiguous -- mixed
            return typeFactory.createType("mixed");
        }

        // parse type string as single or union
        if (unprocessedType.contains("|")) {
            // union type
            List<PhpType> typeList = Arrays.stream(unprocessedType.split("\\|"))
                    .map(typeFactory::createType)
                    .toList();

            return typeFactory.createUnionType(typeList);
        } else {
            // single type
            return typeFactory.createType(unprocessedType);
        }
    }
}
