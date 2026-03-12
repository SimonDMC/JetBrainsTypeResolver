import api.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TypeResolverTest {

    @Test
    public void standardAnonymousType() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                new PhpDocBlock(Collections.singletonMap("@var", List.of(
                        new DocTag("User")
                ))),
                "$user"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(SingleType.class, type);
        assertEquals("User", ((SingleType) type).getTypeName());
    }

    @Test
    public void unionAnonymousType() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                new PhpDocBlock(Collections.singletonMap("@var", List.of(
                        new DocTag("string|int")
                ))),
                "$id"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(UnionType.class, type);
        assertEquals(List.of("string", "int"), ((UnionType) type).getTypes().stream().map(SingleType::getTypeName).toList());
    }

    @Test
    public void standardNamedType() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                new PhpDocBlock(Collections.singletonMap("@var", List.of(
                        new DocTag("Logger $log")
                ))),
                "$log"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(SingleType.class, type);
        assertEquals("Logger", ((SingleType) type).getTypeName());
    }

    @Test
    public void unionNamedType() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                new PhpDocBlock(Collections.singletonMap("@var", List.of(
                        new DocTag("string|int $id")
                ))),
                "$id"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(UnionType.class, type);
        assertEquals(List.of("string", "int"), ((UnionType) type).getTypes().stream().map(SingleType::getTypeName).toList());
    }

    @Test
    public void missingType() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                new PhpDocBlock(Collections.singletonMap("@var", List.of(
                        new DocTag("Admin $adm")
                ))),
                "$guest"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(SingleType.class, type);
        assertEquals("mixed", ((SingleType) type).getTypeName());
    }

    @Test
    public void multipleTypes() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                new PhpDocBlock(Collections.singletonMap("@var", List.of(
                        new DocTag("int $id"),
                        new DocTag("string $name")
                ))),
                "$name"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(SingleType.class, type);
        assertEquals("string", ((SingleType) type).getTypeName());
    }

    @Test
    public void noVarAnnotation() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                new PhpDocBlock(Collections.singletonMap("@returns", List.of(
                        new DocTag("string")
                ))),
                "$id"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(SingleType.class, type);
        assertEquals("mixed", ((SingleType) type).getTypeName());
    }

    @Test
    public void noDocBlock() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable(
                null,
                "$id"
        );

        PhpType type = resolver.inferTypeFromDoc(variable);
        assertInstanceOf(SingleType.class, type);
        assertEquals("mixed", ((SingleType) type).getTypeName());
    }
}
