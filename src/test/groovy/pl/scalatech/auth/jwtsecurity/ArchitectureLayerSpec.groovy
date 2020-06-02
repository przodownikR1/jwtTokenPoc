package pl.scalatech.auth.jwtsecurity

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import org.springframework.context.annotation.Configuration
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import java.lang.Void as Should

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.*
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import static com.tngtech.archunit.library.Architectures.layeredArchitecture

class ArchitectureLayerSpec extends Specification {
    @Shared
    def allClasses = new ClassFileImporter()
            .withImportOption(DO_NOT_INCLUDE_TESTS)
            .withImportOption(DO_NOT_INCLUDE_ARCHIVES)
            .withImportOption(DO_NOT_INCLUDE_JARS)
            .importPackages("pl.scalatech.auth.jwtsecurity")


    Should 'keep an eye on hexagonal architecture rules as infrastructure'() {
        given:
        def rule = noClasses().that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat().resideInAnyPackage("..adapter..")
        expect:
        rule.check(allClasses)
    }

    Should 'keep an eye on hexagonal architecture rules as secondaryAdapter'() {
        given:
        def rule = noClasses().that().resideInAPackage("..adapter.out..")
                .should().dependOnClassesThat().resideInAnyPackage("..adapter.in..")
        expect:
        rule.check(allClasses)
    }

    Should 'keep an eye on hexagonal architecture rules as primaryAdapter'() {
        given:
        def rule = noClasses().that().resideInAPackage("..adapter.in..")
                .should().dependOnClassesThat().resideInAnyPackage("..adapter.out..")
        expect:
        rule.check(allClasses)
    }


    Should "keep overall aritecture layers rules"() {
        given:
        def layers = layeredArchitecture()
                .layer("port").definedBy("..port..")
                .layer("primaryAdapter").definedBy("..adapter.in..")
                .layer("secondaryAdapter").definedBy("..adapter.out..")
                .layer("infrastructure").definedBy("..infrastructure..")
                .whereLayer("primaryAdapter").mayNotBeAccessedByAnyLayer()
                .whereLayer("secondaryAdapter").mayNotBeAccessedByAnyLayer()
                //.whereLayer("infrastructure").mayOnlyBeAccessedByLayers("port")
                .whereLayer("port").mayOnlyBeAccessedByLayers("primaryAdapter", "secondaryAdapter", "infrastructure")

        expect:
        layers.check(allClasses)
    }

    Should "configuration class be @Configuration"() {
        given:
        ArchRule rule = classes().that()
                .haveSimpleNameEndingWith("Config")
                .should().beAnnotatedWith(Configuration.class)
        expect:
        rule.check(allClasses)
    }
}
