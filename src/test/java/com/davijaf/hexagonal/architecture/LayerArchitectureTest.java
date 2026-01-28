package com.davijaf.hexagonal.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.davijaf.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
public class LayerArchitectureTest {

    @ArchTest
    public static final Architectures.LayeredArchitecture LAYER_ARCHITECTURE_TEST =
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
                    .whereLayer("Config").mayNotBeAccessedByAnyLayer();
}
