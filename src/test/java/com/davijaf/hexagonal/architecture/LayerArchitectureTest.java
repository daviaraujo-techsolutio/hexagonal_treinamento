package com.davijaf.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class LayerArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.davijaf.hexagonal");
    }

    @Test
    void shouldRespectHexagonalArchitectureLayers() {
        // Definição das camadas e restrições de acesso
        layeredArchitecture()
                .consideringAllDependencies()
                // Definição das camadas
                .layer("AdapterIn").definedBy("..adapters.in..")
                .layer("AdapterOut").definedBy("..adapters.out..")
                .layer("UseCase").definedBy("..application.core.usecase..")
                .layer("PortIn").definedBy("..application.ports.in..")
                .layer("PortOut").definedBy("..application.ports.out..")
                .layer("Config").definedBy("..config..")

                // Restrições de acesso entre camadas
                // AdapterIn só pode ser acessado por Config
                .whereLayer("AdapterIn").mayOnlyBeAccessedByLayers("Config")

                // AdapterOut só pode ser acessado por Config
                .whereLayer("AdapterOut").mayOnlyBeAccessedByLayers("Config")

                // UseCase só pode ser acessado por Config
                .whereLayer("UseCase").mayOnlyBeAccessedByLayers("Config")

                // PortIn pode ser acessado por UseCase, AdapterIn e Config
                .whereLayer("PortIn").mayOnlyBeAccessedByLayers("UseCase", "AdapterIn", "Config")

                // PortOut pode ser acessado por UseCase, AdapterOut e Config
                .whereLayer("PortOut").mayOnlyBeAccessedByLayers("UseCase", "AdapterOut", "Config")

                // Config não pode ser acessado por nenhuma camada
                .whereLayer("Config").mayNotBeAccessedByAnyLayer()

                .check(importedClasses);
    }
}
