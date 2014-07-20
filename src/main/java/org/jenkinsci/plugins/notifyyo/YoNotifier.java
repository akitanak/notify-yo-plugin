package org.jenkinsci.plugins.notifyyo;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;

import net.sf.json.JSONObject;
import net.takanotsume.yo.YoService;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * YoNotifier {@link Notifier}.
 *
 * @author Akihito Tanaka
 */
public class YoNotifier extends Notifier {
    
    private final String individualToken;
    
    @DataBoundConstructor
    public YoNotifier(String individualToken) {
        this.individualToken = individualToken;
    }
    
    public String getIndividualToken() {
        return individualToken;
    }
    
    public BuildStepMonitor getRequiredMonitorService() {
        
        return BuildStepMonitor.BUILD;
    }
    
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {
        
        if (build.getResult() == Result.FAILURE
                || build.getResult() == Result.UNSTABLE) {
            
            String token = isEmpty(individualToken) ? getDescriptor()
                    .getApitoken() : individualToken;
            
            if (token.isEmpty()) {
                listener.error("No api token was set. cannot send Yo.");
                return true;
            }
            
            try {
                YoService.sendYoAll(token);
            } catch (Exception e) {
                listener.error("Failed to send Yo : " + e.getMessage());
            }
        }
        
        return true;
    }
    
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }
    
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Descriptor for {@link YoNotifier}. Used as a singleton. The class is
     * marked as public so that it can be accessed from views.
     *
     */
    @Extension
    public static final class DescriptorImpl extends
            BuildStepDescriptor<Publisher> {
        
        private String apitoken = null;
        
        public DescriptorImpl() {
            super(YoNotifier.class);
            load();
        }
        
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project
            // types
            return true;
        }
        
        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Notify YO";
        }
        
        @Override
        public boolean configure(StaplerRequest req, JSONObject formData)
                throws FormException {
            apitoken = formData.getString("apitoken");
            
            save();
            return super.configure(req, formData);
        }
        
        public String getApitoken() {
            return apitoken;
        }
        
        public void setApitoken(String apitoken) {
            this.apitoken = apitoken;
        }
        
    }
}
