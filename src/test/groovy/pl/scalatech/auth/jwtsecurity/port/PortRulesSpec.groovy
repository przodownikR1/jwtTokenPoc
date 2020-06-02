package pl.scalatech.auth.jwtsecurity.port

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import spock.lang.Shared
import spock.lang.Specification

import java.lang.Void as Should

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*

class PortRulesSpec extends Specification {
    static final String BASE_PACKAGE = 'pl.scalatech.auth.jwtsecurity'
    static final String PORT_PACKAGE = "${BASE_PACKAGE}.port"
    @Shared
    def ports = new ClassFileImporter()
            .withImportOption(DO_NOT_INCLUDE_TESTS)
            .importPackages(PORT_PACKAGE)

    Should "port have only public methods and classes"() {
        given:
        ArchRule classRule = classes().that()
                .resideInAPackage(PORT_PACKAGE)
                .should().bePublic().andShould().beInterfaces()
        and:
        ArchRule methodRule = methods().that().areDeclaredInClassesThat()
                .resideInAPackage(PORT_PACKAGE).should().bePublic()
        expect:
        classRule.check(ports)
        methodRule.check(ports)
    }

    Should "port not depend on spring"() {
        given:
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(
                        "..port..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.springframework..");
        expect:
        rule.check(ports)
    }
}
