package configurationslicing.maven;

import hudson.Extension;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSet.DescriptorImpl;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import configurationslicing.UnorderedStringSlicer;
import hudson.model.AbstractProject;

@Extension(optional = true)
public class MavenOptsSlicer extends UnorderedStringSlicer<AbstractProject> {

    public MavenOptsSlicer() {
        super(new MavenOptsSlicerSpec());
    }

    @Override
    public void loadPluginDependencyClass() {
        // this is just to demonstrate that the Maven plugin is loaded
        MavenModuleSet.class.getClass();
    }

    public static class MavenOptsSlicerSpec extends UnorderedStringSlicerSpec<AbstractProject> {

        public String getDefaultValueString() {
        	return null;
        }

        public String getName() {
            return "MAVEN_OPTS per Maven project";
        }

        public String getName(AbstractProject item) {
            return item.getFullName();
        }

        public String getUrl() {
            return "mavenopts";
        }

        public List<String> getValues(AbstractProject item) {
            List<String> ret = new ArrayList<String>();
            if(!(item instanceof MavenModuleSet)) {
                return ret;
            }
            String mavenOpts = ((MavenModuleSet)item).getMavenOpts();
            ret.add(mavenOpts);
            return ret;
        }

        @SuppressWarnings("unchecked")
		public List<AbstractProject> getWorkDomain() {
            return (List) Jenkins.get().getAllItems(MavenModuleSet.class);
        }

        public boolean setValues(AbstractProject item, List<String> set) {
            if(set.isEmpty() || !(item instanceof MavenModuleSet)) return false;
            MavenModuleSet mavenItem = (MavenModuleSet)item;
            String value = set.iterator().next();
            DescriptorImpl descriptor =
                    Jenkins.get().getDescriptorByType(MavenModuleSet.DescriptorImpl.class);
            if(value.equals(descriptor.getGlobalMavenOpts())) {
                mavenItem.setMavenOpts(null);
            } else {
                mavenItem.setMavenOpts(value);
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
