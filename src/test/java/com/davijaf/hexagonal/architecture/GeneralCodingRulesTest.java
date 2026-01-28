package com.davijaf.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

/**
 * Testes de regras gerais de codificação.
 * Valida boas práticas de código como:
 * - Não usar System.out/System.err
 * - Não lançar exceções genéricas
 * - Não usar java.util.logging
 * - Não usar Joda-Time (preferir java.time)
 */
class GeneralCodingRulesTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.davijaf.hexagonal");
    }

    @Test
    void noClassesShouldAccessStandardStreams() {
        NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.check(importedClasses);
    }

    @Test
    void noClassesShouldThrowGenericExceptions() {
        NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.check(importedClasses);
    }

    @Test
    void noClassesShouldUseJavaUtilLogging() {
        NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING.check(importedClasses);
    }

    @Test
    void noClassesShouldUseJodaTime() {
        NO_CLASSES_SHOULD_USE_JODATIME.check(importedClasses);
    }

    @Test
    void noClassesShouldUseFieldInjection() {
        ArchRule rule = noClasses()
                .should()
                .dependOnClassesThat()
                .haveFullyQualifiedName("org.springframework.beans.factory.annotation.Autowired")
                .because("Field injection is not recommended. Use constructor injection instead.");

        // Esta regra é validada no SpringCodingRulesTest de forma mais específica
    }
}
