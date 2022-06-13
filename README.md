# semver-plugin
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Actions Status](https://github.com/f-u-z-z-l-e/semver-plugin/workflows/build/badge.svg)](https://github.com/f-u-z-z-l-e/semver-plugin/actions)
[![gradlePluginPortal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/ch/fuzzle/gradle/semver/ch.fuzzle.gradle.semver.gradle.plugin/maven-metadata.xml.svg?label=gradlePluginPortal)](https://plugins.gradle.org/plugin/ch.fuzzle.gradle.semver)
[![codecov](https://codecov.io/gh/f-u-z-z-l-e/semver-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/f-u-z-z-l-e/semver-plugin)

## Summary
This Gradle plugin sets the project version of your project based on your project's
[git tags](https://git-scm.com/book/en/v2/Git-Basics-Tagging) according to the Semantic Versioning
[specification](https://semver.org). It works out of the box, but also offers some sophisticated configuration options.

## Versioning Tasks
The following tasks are added to Gradle:
* tagHeadCommit
  * Tags the head commit on the git repository with the current [evaluated version](#Version-evaluation).

* pushTagToOrigin
  * Pushes local tags to origin (```git push origin --tags```)

* displayVersion
  * Prints the current project version to the console.

## Version evaluation
The current version is evaluated and set as **project.version** in gradles
[configuration build phase](https://docs.gradle.org/current/userguide/build_lifecycle.html#sec:build_phases) unless it
is set explicitly in the build script.

The evaluation checks the git repository for all available tags with the given [prefix](#configuration). Sorts the result and
calculates the next version depending on the latest tag.

```
git tag -l        sorted        latest         next
----------        -------       ------        ------
  v0.1.0          v0.1.0
  v0.1.1          v0.1.1
  v0.1.10         v0.1.2
  v0.1.2          v0.1.5
  v0.1.5          v0.1.7
  v0.1.7   -->    v0.1.8   -->   v0.2.2  -->   v0.2.3
  v0.1.8          v0.1.9
  v0.1.9          v0.1.10
  v0.2.0          v0.2.0
  v0.2.1          v0.2.1
  v0.2.2          v0.2.2
```

### Version promotion
By default the plugin will only increase the patch version as shown above. To promote the minor or major version, the
plugin evaluates the head commit message. If it finds **#minor** or **#major** somewhere in the commit message, it will
increase the corresponding part.

* Increase minor version:
```
git tag -m "This commit will increase the #minor version."

  v0.0.1   -->   v0.1.0
```
* Increase major version:
```
git tag -m "This commit will increase the #major version."

  v0.0.1   -->   v1.0.0
```

### Versions on branches
On branches the plugin will append a **pre-release** and **build metadata** version as defined by the Semantic Versioning
[specification](https://semver.org). This means that the resulting version will be in the following format:

```
major.minor.patch-preRelease+buidMetadata  -->  0.2.1-cov+241dd421
```

Pre-release by default will be set to the current branch, and build metadata is the shortened commit sha. The pre-release
can be overridden via the semver gradle extension.

#### Pre-release format transformation
In order for the branch name to conform to the pre-release syntax the branch name will be transformed as follows:
```
branch name: abc-123 -> pre-release: abc_123
branch name: some-snake-case-name -> pre-release: someSnakeCaseName
```

### Setting a release branch
Some teams might wish to specify a dedicated ***release*** branch. By default the plugin will treat the **master**
branch as release branch. On the release branch the plugin will omit the pre-release and build metadata parts.

### Setting a tag message
The plugin will create an [annotated git tag](https://git-scm.com/book/en/v2/Git-Basics-Tagging) and set a default commit
message. To override the default message one can specify an alternate **tagMessage** via the semver gradle extension. 

### Setting the buildMetadataSeparator
Sometimes it is necessary to tweak the output of the version string as not all projects follow the semver specification
strictly. For example if you would like to tag a docker image with the default settings applied by this plugin, the
docker build command will fail as it doesn't allow the character '+' in the tag name. To circumvent this problem the
buildMetadataSeparator property allows for overriding the separator with a custom value. 

## Configuration
Refer to [plugins.gradle.org](https://plugins.gradle.org/plugin/ch.fuzzle.gradle.semver) on how to apply the plugin.

### Groovy
```groovy
semver {
    prefix = 'v' // sets the version prefix. -> v0.0.1
    preRelease = 'rc1' // sets the pre-release string (default == git branch name)
    releaseBranch = 'master' // sets the git branch name that is considered to be the 'release' branch.
    tagMessage = 'Tagged automatically.' // sets the message the tagHeadCommit task should use (default = 'Tagged by 'ch.fuzzle.gradle.semver' gradle plugin.')
    buildMetadataSeparator = '-' // defines the separator character before buildMetadata. (default = '+')
}
```

### Kotlin
```kotlin
semver {
    prefix.value("v") // sets the version prefix. -> v0.0.1
    preRelease.value("rc1") // sets the pre-release string (default == git branch name)
    releaseBranch.value("master") // sets the git branch name that is considered to be the 'release' branch.
    tagMessage.value("Tagged automatically.") // sets the message the tagHeadCommit task should use (default = 'Tagged by 'ch.fuzzle.gradle.semver' gradle plugin.')
    buildMetadataSeparator.value("-") // defines the separator character before buildMetadata. (default = '+')
}
```

## Usage Example
Conceptually, `displayVersion` shows you the version you are currently working on and `tagHeadCommit` records that
version in the version history (making it permanent).

To increase the minor/major version you create a Git commit with a `#minor`/`#major` marker in the message. You will
then be working on that version.

As this plugin evaluates only the last commit for versioning, any new commit before `tagHeadCommit` that does not have
a marker reverts the minor/major version jump. Simply repeat the `#minor` or `#major` marker in the commit message if
you want additional commits before `tagHeadCommit`.

### Step-by-Step
This example includes publishing your work to an artifact repository. The version displayed by the `displayVersion` task
is the version used for publishing artifacts. The normal sequence is to first publish your artifact and *then* make the
version permanent and official with the `tagHeadCommit` task.

What version are we working on?

    ./gradlew displayVersion        # 0.0.1

Do work on version 0.0.1 and commit to Git:

    git add .
    git commit -m "fixed a bug"
    git push

What version are we going to publish?

    ./gradlew displayVersion        # 0.0.1

Publish version 0.0.1 to an artifact repository:

    ./gradlew publish               # publish 0.0.1 artifact

Now that we have finished and published version 0.0.1, we want it tagged as 0.0.1, i.e. recorded in the version
history:

    ./gradlew tagHeadCommit         # create 0.0.1 tag
    ./gradlew pushTagOrigin         # push 0.0.1 tag to Git
    git tag -l                      # 0.0.1

Other developers can now access our published version 0.0.1 by referring to the corresponding tag.
The creation of the 0.0.1 tag moves us to the next patch version. We are ready to work on version 0.0.2:

    ./gradlew displayVersion        # 0.0.2

We repeat the above steps, publish and tag version 0.0.2: 

    git add .
    git commit -m "fixed another bug"
    git push
    ./gradlew publish               # publish 0.0.2 artifact
    ./gradlew tagHeadCommit         # create 0.0.2 tag
    ./gradlew pushTagOrigin         # push 0.0.2 tag to Git
    git tag -l                      # 0.0.2 + 0.0.1

Now we are ready to work on version 0.0.3:

    ./gradlew displayVersion        # 0.0.3

    git add .
    git commit -m "added new function"
    git push

Hmm, we added (backwards compatible) functionality, so we should increase the minor version before publishing. We do not
want to publish a version 0.0.3, instead we want 0.1.0:

    ./gradlew displayVersion        # 0.0.3 (no, we don't want this)
    git add .
    git commit -m "new function #minor"
    git push

What version are we going to publish?

    ./gradlew displayVersion        # 0.1.0

Oh, no! We forgot something and have to add another commit before publishing 0.1.0. For this we have to repeat the
`#minor` marker in the commit message in order to remain at version 0.1.0:

    git add .
    git commit -m "new function improvement #minor"
    git push

Verify that we are still on 0.1.0 for publishing:

    ./gradlew displayVersion        # 0.1.0

Publish 0.1.0:

    ./gradlew publish               # publish 0.1.0 artifact

Now that we have finished and published version 0.1.0, we want it tagged as 0.1.0 (recorded in the version history):

    ./gradlew tagHeadCommit         # create 0.1.0 tag
    ./gradlew pushTagOrigin         # push 0.1.0 tag to Git
    git tag -l                      # 0.1.0 + 0.0.2 + 0.0.1

And now we are ready to work on 0.1.1:

    ./gradlew displayVersion        # 0.1.1

## Development

### IntelliJ
To run the [SemVerPluginTest](src/test/kotlin/SemVerPluginTest.kt) in IntelliJ under Windows, you may have to set the
PATH variable in the run configuration so that JGit finds Git: 

    Environment variables: PATH=C:\Program Files\Git\bin