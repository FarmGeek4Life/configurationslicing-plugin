package configurationslicing;

import hudson.model.AbstractProject;
import hudson.tasks.LogRotator;

import java.util.ArrayList;
import java.util.List;

import configurationslicing.logrotator.LogRotationSlicer;
import configurationslicing.logrotator.LogRotationSlicer.LogRotationBuildsSliceSpec;
import configurationslicing.logrotator.LogRotationSlicer.LogRotationDaysSliceSpec;
import org.junit.Assert;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

public class LogRotationSlicerTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

	@SuppressWarnings("unchecked")
	public void testSetValues() throws Exception {
		AbstractProject item = j.createFreeStyleProject();
		
	    int daysToKeep = 111;
	    int numToKeep = 222;
	    int artifactDaysToKeep = 333;
	    int artifactNumToKeep = 444;
	    
		LogRotator lr = new LogRotator(daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep);
		assertEquals(lr, daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep);
		
		item.setLogRotator(lr);
		assertEquals(item.getLogRotator(), daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep);
		
		numToKeep = 12345;
		List<String> set = new ArrayList<String>();
		set.add(String.valueOf(numToKeep));
		
		LogRotationBuildsSliceSpec buildsSpec = new LogRotationBuildsSliceSpec();
		buildsSpec.setValues(item, set);
		assertEquals(item.getLogRotator(), daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep);

		
		daysToKeep = 54321;
		set = new ArrayList<String>();
		set.add(String.valueOf(daysToKeep));
		
		LogRotationDaysSliceSpec daysSpec = new LogRotationDaysSliceSpec();
		daysSpec.setValues(item, set);
		assertEquals(item.getLogRotator(), daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep);
	}
	
	private void assertEquals(LogRotator lr, int daysToKeep, int numToKeep, int artifactDaysToKeep, int artifactNumToKeep) {
		Assert.assertEquals(daysToKeep, lr.getDaysToKeep());
		Assert.assertEquals(numToKeep, lr.getNumToKeep());
		Assert.assertEquals(artifactDaysToKeep, lr.getArtifactDaysToKeep());
		Assert.assertEquals(artifactNumToKeep, lr.getArtifactNumToKeep());
	}
	
	public void testLogRotatorEquals() {
		doTestLogRotatorEquals(0, 0, 0, 0, true);
		doTestLogRotatorEquals(-1, -1, -1, -1, true);

		doTestLogRotatorEquals(1, 1, 1, 1, true);
		doTestLogRotatorEquals(1, 1, 2, 2, true);
		
		doTestLogRotatorEquals(1, -1, -1, -1, false);
		doTestLogRotatorEquals(-1, 1, -1, -1, false);
		doTestLogRotatorEquals(-1, -1, 1, -1, false);
		doTestLogRotatorEquals(-1, -1, -1, 1, false);
		
		doTestLogRotatorEquals(1, 1, 2, 3, false);
		doTestLogRotatorEquals(2, 3, 1, 1, false);
	}
	private void doTestLogRotatorEquals(int d1, int d2, int n1, int n2, boolean expect) {
		LogRotator r1 = new LogRotator(d1, n1, 0, 0);
		LogRotator r2 = new LogRotator(d2, n2, 0, 0);
		boolean equals = LogRotationSlicer.equals(r1, r2);
		Assert.assertEquals(expect, equals);
		Assert.assertFalse(LogRotationSlicer.equals(r1, null));
		Assert.assertFalse(LogRotationSlicer.equals(null, r1));
		Assert.assertFalse(LogRotationSlicer.equals(r2, null));
		Assert.assertFalse(LogRotationSlicer.equals(null, r2));
		
		Assert.assertTrue(LogRotationSlicer.equals(null, null));
		Assert.assertTrue(LogRotationSlicer.equals(r1, r1));
		Assert.assertTrue(LogRotationSlicer.equals(r2, r2));
	}
	
}
