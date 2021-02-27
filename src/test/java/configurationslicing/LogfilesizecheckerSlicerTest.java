package configurationslicing;

import configurationslicing.logfilesizechecker.LogfilesizecheckerSlicer;
import hudson.plugins.logfilesizechecker.LogfilesizecheckerWrapper;
import org.junit.Assert;
import org.jvnet.hudson.test.JenkinsRule;
import org.junit.Rule;
import org.junit.Test;

public class LogfilesizecheckerSlicerTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

	int maxLogSize = 3; 
	boolean failBuild = true;
	boolean setOwn = true;
	
    @Test
	public void testNewLogfilesizecheckerWrapper() {
	    LogfilesizecheckerWrapper wrapper = LogfilesizecheckerSlicer.LogfilesizeSliceSpec.newLogfilesizecheckerWrapper(maxLogSize, failBuild, setOwn);
		Assert.assertEquals(maxLogSize, wrapper.maxLogSize);
		Assert.assertEquals(failBuild, wrapper.failBuild);
        Assert.assertEquals(setOwn, wrapper.setOwn);
	}

    @Test
	public void testLogfilesizecheckerWrapperConstructor() {
	    LogfilesizecheckerWrapper wrapper = new LogfilesizecheckerWrapper(maxLogSize, failBuild, setOwn);
		Assert.assertEquals(maxLogSize, wrapper.maxLogSize);
		Assert.assertEquals(failBuild, wrapper.failBuild);
        Assert.assertEquals(setOwn, wrapper.setOwn);
	}
}