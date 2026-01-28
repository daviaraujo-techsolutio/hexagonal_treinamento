package com.davijaf.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;

/**
 * Testes de regras de codificação específicas para Spring.
 * Valida boas práticas como:
 * - Proibir injeção de dependência por campo (@Autowired em campos)
 * - Garantir imutabilidade em componentes Spring
 */
class SpringCodingRulesTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.davijaf.hexagonal");
    }

    @Test
    @Disabled("TODO: Refactor to use constructor injection instead of @Autowired field injection (17 violations)")
    void noFieldsShouldBeAnnotatedWithAutowired() {
        ArchRule rule = noFields()
                .should()
                .beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
                .because("Field injection is not recommended. Use constructor injection instead for better testability and immutability.");

        rule.check(importedClasses);
    }

    @Test
    @Disabled("TODO: Refactor Services to use final fields with constructor injection")
    void springServicesShouldHaveOnlyFinalFields() {
        ArchRule rule = fields()
                .that()
                .areDeclaredInClassesThat()
                .areAnnotatedWith("org.springframework.stereotype.Service")
                .and()
                .areNotStatic()
                .should()
                .beFinal()
                .because("Service classes should be immutable. Use final fields with constructor injection.");

        rule.check(importedClasses);
    }

    @Test
    @Disabled("TODO: Refactor Components to use final fields with constructor injection")
    void springComponentsShouldHaveOnlyFinalFields() {
        ArchRule rule = fields()
                .that()
                .areDeclaredInClassesThat()
                .areAnnotatedWith("org.springframework.stereotype.Component")
                .and()
                .areNotStatic()
                .should()
                .beFinal()
                .because("Component classes should be immutable. Use final fields with constructor injection.");

        rule.check(importedClasses);
    }

    @Test
    @Disabled("TODO: Refactor Repositories to use final fields with constructor injection")
    void springRepositoriesShouldHaveOnlyFinalFields() {
        ArchRule rule = fields()
                .that()
                .areDeclaredInClassesThat()
                .areAnnotatedWith("org.springframework.stereotype.Repository")
                .and()
                .areNotStatic()
                .should()
                .beFinal()
                .because("Repository classes should be immutable. Use final fields with constructor injection.");

        rule.check(importedClasses);
    }

    @Test
    @Disabled("TODO: Refactor RestControllers to use final fields with constructor injection")
    void restControllersShouldHaveOnlyFinalFields() {
        ArchRule rule = fields()
                .that()
                .areDeclaredInClassesThat()
                .areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .and()
                .areNotStatic()
                .should()
                .beFinal()
                .because("RestController classes should be immutable. Use final fields with constructor injection.");

        rule.check(importedClasses);
    }
}
