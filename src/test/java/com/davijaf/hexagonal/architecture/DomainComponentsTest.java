package com.davijaf.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Testes de componentes de domínio.
 * Valida que o domínio (core) permanece isolado e não depende de:
 * - Adapters (in/out)
 * - Frameworks externos (Spring, etc.)
 * - Infraestrutura
 */
class DomainComponentsTest {

    private static final String DOMAIN_PACKAGE = "..application.core.domain..";
    private static final String USECASE_PACKAGE = "..application.core.usecase..";
    private static final String PORTS_PACKAGE = "..application.ports..";
    private static final String ADAPTERS_PACKAGE = "..adapters..";
    private static final String CONFIG_PACKAGE = "..config..";

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.davijaf.hexagonal");
    }

    @Test
    void domainShouldNotDependOnAdapters() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(DOMAIN_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAPackage(ADAPTERS_PACKAGE)
                .because("Domain should not depend on adapters. This violates the hexagonal architecture.");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotDependOnConfig() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(DOMAIN_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAPackage(CONFIG_PACKAGE)
                .because("Domain should not depend on configuration classes.");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotDependOnSpringFramework() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(DOMAIN_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAPackage("org.springframework..")
                .because("Domain should be framework-agnostic and not depend on Spring.");

        rule.check(importedClasses);
    }

    @Test
    void useCasesShouldNotDependOnAdapters() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(USECASE_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAPackage(ADAPTERS_PACKAGE)
                .because("Use cases should not depend on adapters. They should only depend on ports.");

        rule.check(importedClasses);
    }

    @Test
    void useCasesShouldNotDependOnConfig() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(USECASE_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAPackage(CONFIG_PACKAGE)
                .because("Use cases should not depend on configuration classes.");

        rule.check(importedClasses);
    }

    @Test
    void portsShouldNotDependOnAdapters() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(PORTS_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAPackage(ADAPTERS_PACKAGE)
                .because("Ports should not depend on adapters. Adapters implement ports, not the other way around.");

        rule.check(importedClasses);
    }

    @Test
    void domainClassesShouldBeAccessedOnlyByApplicationAndPorts() {
        ArchRule rule = classes()
                .that()
                .resideInAPackage(DOMAIN_PACKAGE)
                .should()
                .onlyBeAccessed()
                .byAnyPackage(
                        DOMAIN_PACKAGE,
                        USECASE_PACKAGE,
                        PORTS_PACKAGE,
                        ADAPTERS_PACKAGE,
                        CONFIG_PACKAGE
                )
                .because("Domain classes should be accessed by application core and adapters only.");

        rule.check(importedClasses);
    }
}
