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

updateCenter.updateAllSites()

def plugins =[
  "workflow-aggregator",
"workflow-cps",
"pipeline-stage-view",
"pipeline-model-definition",
"git",
"github-branch-source",
"gitlab-plugin",
"blueocean",
"pipeline-github-lib",
"credentials-binding",
"docker-plugin"
]

def installed = false

plugins.each {
  if (!pluginManager.getPlugin(it)) {
    logger.info("Installing " + it)
    def plugin = updateCenter.getPlugin(it)
    if (plugin) {
      plugin.deploy()
      installed = true
    }
  }
}

if (installed) {
  logger.info("Plugins installed, initializing Jenkins")
  jenkins.save()
  jenkins.restart()
}


import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('admin','password')
instance.setSecurityRealm(hudsonRealm)
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()
