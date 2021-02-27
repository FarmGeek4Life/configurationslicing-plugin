package configurationslicing.claim;

import jenkins.model.Jenkins;
import hudson.maven.MavenModuleSet;
import hudson.model.AbstractProject;
import hudson.tasks.Maven.MavenInstallation;

import org.junit.Test;

import configurationslicing.claim.ClaimSlicer.ClaimSpec;
import configurationslicing.claim.ClaimSlicer;
import org.junit.Assert;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;



public class ClaimSlicerTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    /*
     * Test that we can interrogate and set values using the claim slicer on free style projects
     */
    @Test
    public void testFreeStyleValues() throws Exception {
        AbstractProject item = j.createFreeStyleProject();
        doTestValues(item);
    }

    /*
     * Test that we can interrogate and set values using the claim slicer on maven projects
     */

    @Test
    public void testMavenValues() throws Exception {
        String name = "test"+j.getInstance().getItems().size();
        MavenModuleSet mavenModuleSet = Jenkins.get().createProject(MavenModuleSet.class,name);
        mavenModuleSet.setRunHeadless( true );

        AbstractProject item = mavenModuleSet;
        doTestValues(item);
    }

    /*
     * Test that the is loaded method returns true if the claim plug-in is loaded.
     * The test that this method returns false otherwise is problematic to test meaningfully in the unit test environment
     */
    @Test
    public void testIsLoaded() {
        ClaimSlicer slicer = new ClaimSlicer();
        boolean isLoaded = slicer.isLoaded();
        Assert.assertTrue("Expect claim slicer to be loaded when we have the claim plugin",isLoaded);
    }


    private void doTestValues(AbstractProject item) {
        ClaimSpec spec = new ClaimSpec();
        boolean claimsEnabled = spec.getValue(item);
        Assert.assertFalse("Claims should be disabled on a new project",claimsEnabled);

        boolean valueSet = spec.setValue(item, false);
        Assert.assertTrue("disabling a value when it is already disabled should work", valueSet);

        valueSet = spec.setValue(item,true);
        Assert.assertTrue("setting a value when it is disabled should work", valueSet);

        claimsEnabled = spec.getValue(item);
        Assert.assertTrue("Claims should be enabled after they have been set",claimsEnabled);

        valueSet = spec.setValue(item,true);
        Assert.assertTrue("setting a value when it is already enabled should work", valueSet);

        valueSet = spec.setValue(item,false);
        Assert.assertTrue("removing the publisher when it is enabled should work", valueSet);

        claimsEnabled = spec.getValue(item);
        Assert.assertFalse("Claims should be disabled after they have been unset",claimsEnabled);        

    }

}
