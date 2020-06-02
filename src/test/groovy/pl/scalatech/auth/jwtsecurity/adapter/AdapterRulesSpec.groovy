package pl.scalatech.auth.jwtsecurity.adapter

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.Void as Should

class AdapterRulesSpec extends Specification {
    static final String BASE_PACKAGE = 'pl.scalatech.auth.jwtsecurity'
    static final String ADAPTER_PACKAGE = "${BASE_PACKAGE}.adapter"
    static final String API_PACKAGE = "${BASE_PACKAGE}.adapter.in.web.."

    @Shared
    def controllerClasses = new ClassFileImporter()
            .importPackages(API_PACKAGE)

    Should "validate_controller_package_naming"() {
        given:

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Controller").should()
                .haveOnlyFinalFields()
                .andShould()
                .resideInAPackage(API_PACKAGE)
                .andShould().beAnnotatedWith(RestController.class)
                .andShould().beAnnotatedWith(RequestMapping.class)
                .orShould().beAnnotatedWith(GetMapping.class)
                .orShould().beAnnotatedWith(PostMapping.class)
                .orShould().beAnnotatedWith(PutMapping.class)
                .orShould().beAnnotatedWith(PatchMapping.class)
                .orShould().beAnnotatedWith(DeleteMapping.class)
                .andShould().notBePublic()


        expect:
        rule.check(controllerClasses)
    }

    Should "validate web package around naming rules"() {
        expect:
        requestsAndResponsesAreNotPublic.check(controllerClasses)
    }

    Should "adapter have only package private scope class"() {
        given:
        def adapters = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(ADAPTER_PACKAGE)
        ArchRule classRule = ArchRuleDefinition.classes().that()
                .resideInAPackage(ADAPTER_PACKAGE).should().bePackagePrivate()
        expect:
        classRule.check(adapters)
    }

    @Unroll
    Should 'Autowired annotations is not used on #DESCRIPTION'() {
        given:
        def rule = CODE_UNIT.should()
                .notBeAnnotatedWith(Autowired.class)
        expect:
        rule.check(controllerClasses)
        where:
        CODE_UNIT                         || DESCRIPTION
        ArchRuleDefinition.constructors() || 'constructors'
        ArchRuleDefinition.fields()       || 'fields'
        ArchRuleDefinition.methods()      || 'methods'
    }

    ArchRule requestsAndResponsesAreNotPublic = ArchRuleDefinition.classes()
            .that().haveSimpleNameEndingWith("Response").or().haveSimpleNameEndingWith("Request")
            .should()
            .notBePublic().andShould().resideInAPackage("..web..").orShould().resideInAPackage("..api..");

}
