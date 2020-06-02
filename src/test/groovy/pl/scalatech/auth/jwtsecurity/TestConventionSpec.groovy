package pl.scalatech.auth.jwtsecurity

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import spock.lang.Shared
import spock.lang.Specification

import java.lang.Void as Should

class TestConventionSpec extends Specification {

    @Shared
    def allClasses = new ClassFileImporter().importPackages("pl.scalatech.auth.jwtsecurity")

    Should "all test classes follow naming convention"() {
        given:
        def rule = ArchRuleDefinition.classes().that()
                .areAssignableTo(Specification.class)
                .should().haveNameMatching(".*Spec\$")
                .orShould().haveNameMatching(".ITSpec\$")
        expect:
        rule.check(allClasses)
    }

}
