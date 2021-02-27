package configurationslicing.maven;

import hudson.Extension;
import hudson.maven.MavenModuleSet;

import java.util.List;

import jenkins.model.Jenkins;
import configurationslicing.BooleanSlicer;
import hudson.model.AbstractProject;

@Extension(optional = true)
public class MavenIncremental extends BooleanSlicer<AbstractProject> {

    public MavenIncremental() {
        super(new MavenIncrementalSlicerSpec());
    }

    @Override
    public boolean isLoaded() {
        return Jenkins.get().getPlugin("maven-plugin") != null;
    }

    public static class MavenIncrementalSlicerSpec implements BooleanSlicerSpec<AbstractProject> {

        public String getName() {
            return "Maven Incremental Build";
        }

        public String getName(AbstractProject item) {
            return item.getFullName();
        }

        public String getUrl() {
            return "mavenincremental";
        }

        @SuppressWarnings("unchecked")
		public List<AbstractProject> getWorkDomain() {
            return (List) Jenkins.get().getAllItems(MavenModuleSet.class);
        }

		@Override
		public boolean getValue(AbstractProject item) {
            if (!(item instanceof MavenModuleSet)) return false;
			return ((MavenModuleSet)item).isIncrementalBuild();
		}

		@Override
		public boolean setValue(AbstractProject item, boolean value) {
            if (!(item instanceof MavenModuleSet)) return false;
			((MavenModuleSet)item).setIncrementalBuild(value);
			return true;
		}

    }
}
