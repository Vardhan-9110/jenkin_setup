def plugins = [
    'workflow-aggregator',
    'workflow-cps',
    'pipeline-stage-view',
    'pipeline-model-definition',
    'git',
    'github-branch-source',
    'gitlab-plugin',
    'blueocean',
    'pipeline-github-lib',
    'credentials-binding'
]
def instance = Jenkins.getInstance()

// Get the plugin manager and update center
def pm = instance.getPluginManager()
def uc = instance.getUpdateCenter()
plugins.each { plugin ->
    if (!pm.getPlugin(plugin)) {
        println("Installing plugin: ${plugin}")
        def installFuture = uc.getPlugin(plugin).deploy()
        installFuture.get()
    } else {
        println("Plugin ${plugin} is already installed.")
    }

instance.save()
