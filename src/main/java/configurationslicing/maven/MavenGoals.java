package configurationslicing.maven;

import hudson.Extension;
import hudson.maven.MavenModuleSet;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import configurationslicing.UnorderedStringSlicer;
import hudson.model.AbstractProject;

@Extension(optional = true)
public class MavenGoals extends UnorderedStringSlicer<AbstractProject> {

    public MavenGoals() {
        super(new MavenGoalsSlicerSpec());
    }

    @Override
    public void loadPluginDependencyClass() {
        // this is just to demonstrate that the Maven plugin is loaded
        MavenModuleSet.class.getClass();
    }

    public static class MavenGoalsSlicerSpec extends UnorderedStringSlicerSpec<AbstractProject> {
        private static final String DEFAULT = "(Default)";

        @Override
        public String getDefaultValueString() {
        	return DEFAULT;
        }

        @Override
        public String getName() {
            return "Maven Goals and Options (Maven project)";
        }

        @Override
        public String getName(AbstractProject item) {
            return item.getFullName();
        }

        @Override
        public String getUrl() {
            return "mavengoals";
        }

        @Override
        public List<String> getValues(AbstractProject item) {
            List<String> ret = new ArrayList<String>();
            if (!(item instanceof MavenModuleSet)) {
                return ret;
            }
            String goals = ((MavenModuleSet)item).getUserConfiguredGoals();
            ret.add(goals == null ? DEFAULT : goals);
            return ret;
        }

        @SuppressWarnings("unchecked")
        @Override
		public List<AbstractProject> getWorkDomain() {
            return (List) Jenkins.get().getAllItems(MavenModuleSet.class);
        }

        @Override
        public boolean setValues(AbstractProject item, List<String> set) {
            if(set.isEmpty() || !(item instanceof MavenModuleSet)) return false;
            MavenModuleSet mavenItem = (MavenModuleSet)item;
            String value = set.iterator().next();
            if(DEFAULT.equalsIgnoreCase(value)) {
                mavenItem.setGoals(null);
            } else {
                mavenItem.setGoals(value);
            }
            try {
                item.save();
            } catch (IOException e) {
                return false;
            }
            return true;
        }

    }
}
