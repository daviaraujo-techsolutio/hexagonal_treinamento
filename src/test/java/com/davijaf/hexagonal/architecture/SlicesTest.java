package com.davijaf.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Testes de slices arquiteturais.
 * Valida:
 * - Ausência de dependências cíclicas entre camadas
 * - Independência entre adaptadores (in e out não devem depender um do outro)
 */
class SlicesTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.davijaf.hexagonal");
    }

    @Test
    void layersShouldBeFreeOfCycles() {
        ArchRule rule = slices()
                .matching("com.davijaf.hexagonal.(*)..")
                .should()
                .beFreeOfCycles()
                .because("Cyclic dependencies between layers violate the hexagonal architecture principles.");

        rule.check(importedClasses);
    }

    @Test
    void adaptersShouldNotDependOnEachOther() {
        ArchRule rule = slices()
                .matching("com.davijaf.hexagonal.adapters.(*)..")
                .should()
                .notDependOnEachOther()
                .because("Adapters (in and out) should be independent and not depend on each other.");

        rule.check(importedClasses);
    }

    @Test
    void inputAdaptersShouldNotDependOnEachOther() {
        ArchRule rule = slices()
                .matching("com.davijaf.hexagonal.adapters.in.(*)..")
                .should()
                .notDependOnEachOther()
                .because("Input adapters (controller, consumer) should be independent.");

        rule.check(importedClasses);
    }

    @Test
    void outputAdaptersShouldNotDependOnEachOther() {
        ArchRule rule = slices()
                .matching("com.davijaf.hexagonal.adapters.out.(*)..")
                .should()
                .notDependOnEachOther()
                .because("Output adapters (repository, client, producer) should be independent.");

        rule.check(importedClasses);
    }
}
