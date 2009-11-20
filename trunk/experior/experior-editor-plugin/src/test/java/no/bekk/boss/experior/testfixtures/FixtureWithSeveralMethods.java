package no.bekk.boss.experior.testfixtures;

import fit.Fixture;
import fitlibrary.DoFixture;

public class FixtureWithSeveralMethods extends DoFixture {
    public void cMethod() {
    }

    public void aMethod() {
    }

    public void dMethod() {
    }

    public void bMethod() {
    }

    @SuppressWarnings("unused")
    private void notIncludePrivateMethod() {
    }

    @SuppressWarnings("unused")
    private void notIncludeProtectedMethod() {
    }

    public Fixture getStoredEntities() {
        return null;
    }
}