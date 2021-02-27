package configurationslicing.tools;

import hudson.plugins.gradle.Gradle;
import hudson.plugins.gradle.Gradle.DescriptorImpl;
import hudson.tasks.Builder;
import hudson.tools.ToolInstallation;

/**
 * @author Maarten Dirkse
 */
public class GradleSlicer extends AbstractToolSlicer {

	public GradleSlicer() {
		super(new GradleSlicerSpec());
	}

	@Override
	protected Class<? extends Builder> getPluginClass() {
		return Gradle.class;
	}

	public static class GradleSlicerSpec extends AbstractToolSlicerSpec {
		@Override
		public String getDefaultValueString() {
			return "(Default)";
		}

		@Override
		public String getName() {
			return "Gradle version per project";
		}

		@Override
		public String getUrl() {
			return "projectgradle";
		}

		@Override
		protected Class<? extends Builder> getBuilderClass() {
			return Gradle.class;
		}

		@Override
		protected ToolInstallation[] getToolInstallations() {
			return new DescriptorImpl().getInstallations();
		}

		@Override
		protected Builder getNewBuilder(Builder oldBuilder,
				String toolInstallationName) {
			final Gradle oldGradle = (Gradle) oldBuilder;
            Gradle newGradle = new Gradle();
            newGradle.setSwitches(oldGradle.getSwitches());
            newGradle.setTasks(oldGradle.getTasks());
            newGradle.setRootBuildScriptDir(oldGradle.getRootBuildScriptDir());
            newGradle.setBuildFile(oldGradle.getBuildFile());
            newGradle.setGradleName(toolInstallationName);
            newGradle.setUseWrapper(oldGradle.isUseWrapper());
            newGradle.setMakeExecutable(oldGradle.isMakeExecutable());
            newGradle.setWrapperLocation(oldGradle.getRootBuildScriptDir());
            newGradle.setUseWorkspaceAsHome(oldGradle.isUseWorkspaceAsHome());
            newGradle.setPassAllAsProjectProperties(oldGradle.isPassAllAsProjectProperties());
            newGradle.setPassAllAsSystemProperties(oldGradle.isPassAllAsSystemProperties());
			return newGradle;
		}

		@Override
		protected String getToolName(Builder builder) {
			return ((Gradle) builder).getGradleName();
		}
	}
}
