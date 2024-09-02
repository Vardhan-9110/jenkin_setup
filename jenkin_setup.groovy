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
  "cloudbees-folder",
  "antisamy-markup-formatter",
  "build-timeout",
  "credentials-binding",
  "timestamper",
  "ws-cleanup",
  "ant",
  "gradle",
  "workflow-aggregator",
  "github-branch-source",
  "pipeline-github-lib",
  "pipeline-graph-view",
  "git",
  "ssh-slaves",
  "matrix-auth",
  "pam-auth",
  "ldap",
  "email-ext",
  "mailer",
  "dark-theme"
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
