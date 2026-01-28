package com.davijaf.hexagonal.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.davijaf.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
public class NamingConventionTest {

        // Consumer deve residir apenas no pacote adapters.in.consumer
        @ArchTest
        public static final ArchRule CONSUMER_RESIDE_ONLY_IN_CONSUMER_PACKAGE = classes()
                        .that().haveNameMatching(".*Consumer")
                        .should().resideInAPackage("..adapters.in.consumer")
                        .as("Consumer classes should reside inside consumer package in adapters.in package");

        // Mapper pode residir em múltiplos pacotes
        @ArchTest
        public static final ArchRule MAPPER_RESIDE_ONLY_IN_MAPPER_PACKAGE = classes()
                        .that().haveNameMatching(".*Mapper")
                        .should().resideInAnyPackage(
                                        "..adapters.in.consumer.mapper",
                                        "..adapters.in.controller.mapper",
                                        "..adapters.out.client.mapper",
                                        "..adapters.out.repository.mapper")
                        .as("Mapper classes should reside inside mapper package in controller, client, consumer or repository packages");

        // Message deve residir apenas no pacote adapters.in.consumer.message
        @ArchTest
        public static final ArchRule MESSAGE_RESIDE_ONLY_IN_MESSAGE_PACKAGE = classes()
                        .that().haveNameMatching(".*Message")
                        .should().resideInAPackage("..adapters.in.consumer.message")
                        .as("Message classes should reside inside message package in adapters.in.consumer package");

        // Controller deve residir apenas no pacote adapters.in.controller
        @ArchTest
        public static final ArchRule CONTROLLER_RESIDE_ONLY_IN_CONTROLLER_PACKAGE = classes()
                        .that().haveNameMatching(".*Controller")
                        .should().resideInAPackage("..adapters.in.controller")
                        .as("Controller classes should reside inside controller package in adapters.in package");

        // Request pode residir em múltiplos pacotes
        @ArchTest
        public static final ArchRule REQUEST_RESIDE_ONLY_IN_REQUEST_PACKAGE = classes()
                        .that().haveNameMatching(".*Request")
                        .should().resideInAnyPackage(
                                        "..adapters.in.controller.request",
                                        "..adapters.out.client.request")
                        .as("Request classes should reside inside request package in controller or client packages");

        // Response pode residir em múltiplos pacotes
        @ArchTest
        public static final ArchRule RESPONSE_RESIDE_ONLY_IN_RESPONSE_PACKAGE = classes()
                        .that().haveNameMatching(".*Response")
                        .should().resideInAnyPackage(
                                        "..adapters.in.controller.response",
                                        "..adapters.out.client.response")
                        .as("Response classes should reside inside response package in controller or client packages");

        // Adapter deve residir apenas no pacote adapters.out
        @ArchTest
        public static final ArchRule ADAPTER_RESIDE_ONLY_IN_ADAPTER_PACKAGE = classes()
                        .that().haveNameMatching(".*Adapter")
                        .should().resideInAPackage("..adapters.out..")
                        .as("Adapter classes should reside inside adapters.out package");

        // Client deve residir apenas no pacote adapters.out.client
        @ArchTest
        public static final ArchRule CLIENT_RESIDE_ONLY_IN_CLIENT_PACKAGE = classes()
                        .that().haveNameMatching(".*Client")
                        .should().resideInAPackage("..adapters.out.client")
                        .as("Client classes should reside inside client package in adapters.out package");

        // Repository deve residir apenas no pacote adapters.out.repository
        @ArchTest
        public static final ArchRule REPOSITORY_RESIDE_ONLY_IN_REPOSITORY_PACKAGE = classes()
                        .that().haveNameMatching(".*Repository")
                        .should().resideInAPackage("..adapters.out.repository")
                        .as("Repository classes should reside inside repository package in adapters.out package");

        // Entity deve residir apenas no pacote adapters.out.repository.entity
        @ArchTest
        public static final ArchRule ENTITY_RESIDE_ONLY_IN_ENTITY_PACKAGE = classes()
                        .that().haveNameMatching(".*Entity")
                        .should().resideInAPackage("..adapters.out.repository.entity")
                        .as("Entity classes should reside inside entity package in adapters.out.repository package");

        // UseCase deve residir apenas no pacote application.core.usecase
        @ArchTest
        public static final ArchRule USE_CASE_RESIDE_ONLY_IN_USE_CASE_PACKAGE = classes()
                        .that().haveNameMatching(".*UseCase")
                        .should().resideInAPackage("..application.core.usecase")
                        .as("UseCase classes should reside inside usecase package in application.core package");

        // InputPort deve residir apenas no pacote application.ports.in
        @ArchTest
        public static final ArchRule INPUT_PORT_RESIDE_ONLY_IN_INPUT_PORT_PACKAGE = classes()
                        .that().haveNameMatching(".*InputPort")
                        .should().resideInAPackage("..application.ports.in")
                        .as("InputPort classes should reside inside in package in application.ports package");

        // OutputPort deve residir apenas no pacote application.ports.out
        @ArchTest
        public static final ArchRule OUTPUT_PORT_RESIDE_ONLY_IN_OUTPUT_PORT_PACKAGE = classes()
                        .that().haveNameMatching(".*OutputPort")
                        .should().resideInAPackage("..application.ports.out")
                        .as("OutputPort classes should reside inside out package in application.ports package");

        // Config deve residir apenas no pacote config
        @ArchTest
        public static final ArchRule CONFIG_RESIDE_ONLY_IN_CONFIG_PACKAGE = classes()
                        .that().haveNameMatching(".*Config")
                        .should().resideInAPackage("..config")
                        .as("Config classes should reside inside config package");

        // Exception deve residir apenas no pacote application.core.exceptions
        @ArchTest
        public static final ArchRule EXCEPTION_RESIDE_ONLY_IN_EXCEPTIONS_PACKAGE = classes()
                        .that().haveNameMatching(".*Exception")
                        .should().resideInAPackage("..application.core.exceptions")
                        .as("Exception classes should reside inside exceptions package in application.core package");

        // Handler deve residir apenas no pacote adapters.in.controller.handler
        @ArchTest
        public static final ArchRule HANDLER_RESIDE_ONLY_IN_HANDLER_PACKAGE = classes()
                        .that().haveNameMatching(".*Handler")
                        .should().resideInAPackage("..adapters.in.controller.handler")
                        .as("Handler classes should reside inside handler package in adapters.in.controller package");

        // =========================================================================
        // TESTES DE SUFIXO OBRIGATÓRIO
        // Garantem que classes em determinados pacotes tenham os sufixos corretos
        // =========================================================================

        // Classes no pacote consumer devem ter sufixo Consumer
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_CONSUMER = classes()
                        .that().resideInAPackage("..adapters.in.consumer")
                        .should().haveSimpleNameEndingWith("Consumer")
                        .as("Classes in consumer package should have suffix 'Consumer'");

        // Classes no pacote mapper devem ter sufixo Mapper ou MapperImpl
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_MAPPER = classes()
                        .that().resideInAPackage("..mapper")
                        .should().haveSimpleNameEndingWith("Mapper")
                        .orShould().haveSimpleNameEndingWith("MapperImpl")
                        .as("Classes in mapper package should have suffix 'Mapper' or 'MapperImpl'");

        // Classes no pacote message devem ter sufixo Message
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_MESSAGE = classes()
                        .that().resideInAPackage("..adapters.in.consumer.message")
                        .should().haveSimpleNameEndingWith("Message")
                        .as("Classes in message package should have suffix 'Message'");

        // Classes no pacote controller devem ter sufixo Controller
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_CONTROLLER = classes()
                        .that().resideInAPackage("..adapters.in.controller")
                        .should().haveSimpleNameEndingWith("Controller")
                        .as("Classes in controller package should have suffix 'Controller'");

        // Classes no pacote request devem ter sufixo Request
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_REQUEST = classes()
                        .that().resideInAPackage("..request")
                        .should().haveSimpleNameEndingWith("Request")
                        .as("Classes in request package should have suffix 'Request'");

        // Classes no pacote response devem ter sufixo Response
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_RESPONSE = classes()
                        .that().resideInAPackage("..response")
                        .should().haveSimpleNameEndingWith("Response")
                        .as("Classes in response package should have suffix 'Response'");

        // Classes no pacote client devem ter sufixo Client ou Adapter
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_CLIENT = classes()
                        .that().resideInAPackage("..adapters.out.client")
                        .should().haveSimpleNameEndingWith("Client")
                        .orShould().haveSimpleNameEndingWith("Adapter")
                        .as("Classes in client package should have suffix 'Client' or 'Adapter'");

        // Classes no pacote repository devem ter sufixo Repository ou Adapter
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_REPOSITORY = classes()
                        .that().resideInAPackage("..adapters.out.repository")
                        .should().haveSimpleNameEndingWith("Repository")
                        .orShould().haveSimpleNameEndingWith("Adapter")
                        .as("Classes in repository package should have suffix 'Repository' or 'Adapter'");

        // Classes no pacote entity devem ter sufixo Entity
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_ENTITY = classes()
                        .that().resideInAPackage("..adapters.out.repository.entity")
                        .should().haveSimpleNameEndingWith("Entity")
                        .as("Classes in entity package should have suffix 'Entity'");

        // Classes no pacote usecase devem ter sufixo UseCase
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_USE_CASE = classes()
                        .that().resideInAPackage("..application.core.usecase")
                        .should().haveSimpleNameEndingWith("UseCase")
                        .as("Classes in usecase package should have suffix 'UseCase'");

        // Classes no pacote ports.in devem ter sufixo InputPort
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_INPUT_PORT = classes()
                        .that().resideInAPackage("..application.ports.in")
                        .should().haveSimpleNameEndingWith("InputPort")
                        .as("Classes in ports.in package should have suffix 'InputPort'");

        // Classes no pacote ports.out devem ter sufixo OutputPort
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_OUTPUT_PORT = classes()
                        .that().resideInAPackage("..application.ports.out")
                        .should().haveSimpleNameEndingWith("OutputPort")
                        .as("Classes in ports.out package should have suffix 'OutputPort'");

        // Classes no pacote config devem ter sufixo Config
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_CONFIG = classes()
                        .that().resideInAPackage("..config")
                        .should().haveSimpleNameEndingWith("Config")
                        .as("Classes in config package should have suffix 'Config'");

        // Classes no pacote exceptions devem ter sufixo Exception
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_EXCEPTION = classes()
                        .that().resideInAPackage("..application.core.exceptions")
                        .should().haveSimpleNameEndingWith("Exception")
                        .as("Classes in exceptions package should have suffix 'Exception'");

        // Classes no pacote handler devem ter sufixo Handler ou Error
        @ArchTest
        public static final ArchRule SHOULD_BE_SUFFIXED_HANDLER = classes()
                        .that().resideInAPackage("..adapters.in.controller.handler")
                        .should().haveSimpleNameEndingWith("Handler")
                        .orShould().haveSimpleNameEndingWith("Error")
                        .as("Classes in handler package should have suffix 'Handler' or 'Error'");
}
