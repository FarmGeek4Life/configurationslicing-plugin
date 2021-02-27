package configurationslicing.maven;

import configurationslicing.BooleanSlicer;
import hudson.Extension;
import hudson.maven.MavenModuleSet;
import hudson.model.AbstractProject;
import jenkins.model.Jenkins;

import java.util.List;

/**
 * A simple boolean configuration slicer to set the "Build whenever a SNAPSHOT dependency is built" build trigger in Maven projects
 * Created by Jeff Bischoff on 4/12/16.
 */
@Extension(optional = true)
public class MavenSnapshotBuildTrigger extends BooleanSlicer<AbstractProject> {

    public MavenSnapshotBuildTrigger() {
        super(new MavenSnapshotBuildTriggerSlicerSpec());
    }

    public static class MavenSnapshotBuildTriggerSlicerSpec implements BooleanSlicerSpec<AbstractProject> {

        @Override
        public String getName() {
            return "Maven Snapshot dependency Build Trigger";
        }

        @Override
        public String getName(AbstractProject item) {
            return item.getFullName();
        }

        @Override
        public String getUrl() {
            return "mavenSnapshotBuildTrigger";
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<AbstractProject> getWorkDomain() {
            return (List) Jenkins.get().getAllItems(MavenModuleSet.class);
        }

        @Override
        public boolean getValue(AbstractProject item) {
            if (!(item instanceof MavenModuleSet)) {
                return false;
            }
            // The UI displays the box checked if upstream builds are *not* ignored, so this slicer should match that behavior
            return ! ((MavenModuleSet)item).ignoreUpstremChanges();
        }

        @Override
        public boolean setValue(AbstractProject item, boolean value) {            
            if(!(item instanceof MavenModuleSet)) return false;
            MavenModuleSet mavenItem = (MavenModuleSet)item;
            // The UI displays the box checked if upstream builds are *not* ignored, so this slicer should match that behavior
            boolean ignored = ! value;
            mavenItem.setIgnoreUpstremChanges(ignored);
            return true;
        }

    }
}
