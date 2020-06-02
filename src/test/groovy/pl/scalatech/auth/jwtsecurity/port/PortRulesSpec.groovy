package pl.scalatech.auth.jwtsecurity.port

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import spock.lang.Specification

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.*
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods

class PortRulesSpec extends Specification {
    static final String BASE_PACKAGE = 'pl.scalatech.auth.jwtsecurity'
    static final String PORT_PACKAGE = "${BASE_PACKAGE}.port"

    Void "port have only public methods and classes"() {
        given:
        def ports = new ClassFileImporter()
                .withImportOption(DO_NOT_INCLUDE_TESTS)
                .importPackages(PORT_PACKAGE)
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
}
