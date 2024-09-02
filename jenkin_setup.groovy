import jenkins.model.*
import hudson.util.*
import jenkins.install.*
import java.util.logging.Logger

def logger = Logger.getLogger("")
def jenkins = Jenkins.getInstance()

// Set Jenkins installation state to avoid setup wizard
jenkins.setInstallState(InstallState.INITIAL_SETUP_COMPLETED)

def pluginManager = jenkins.getPluginManager()
def updateCenter = jenkins.getUpdateCenter()

// Update plugin sites
updateCenter.updateAllSites()

def plugins = [
    "git", 
    "workflow-aggregator", 
    "docker-plugin", 
    "workflow-cps", 
    "pipeline-stage-view", 
    "pipeline-model-definition", 
    "github-branch-source", 
    "gitlab-plugin", 
    "blueocean", 
    "pipeline-github-lib", 
    "credentials-binding"
]
def installed = false

plugins.each { plugin ->
    if (!pluginManager.getPlugin(plugin)) {
        logger.info("Installing plugin: ${plugin}")
        def pluginToInstall = updateCenter.getPlugin(plugin)
        if (pluginToInstall) {
            try {
                pluginToInstall.deploy().get() // Wait for installation to complete
                installed = true
                logger.info("Plugin ${plugin} installed successfully.")
            } catch (Exception e) {
                logger.severe("Failed to install plugin ${plugin}: ${e.message}")
            }
        } else {
            logger.warning("Plugin ${plugin} not found in update center.")
        }
    } else {
        logger.info("Plugin ${plugin} is already installed.")
    }
}

if (installed) {
    logger.info("Plugins installed, initializing Jenkins")
    jenkins.save()
    jenkins.restart()
}
