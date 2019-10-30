# semver-plugin
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Actions Status](https://github.com/f-u-z-z-l-e/semver-plugin/workflows/build/badge.svg)](https://github.com/f-u-z-z-l-e/semver-plugin/actions)
[![gradlePluginPortal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/ch/fuzzle/gradle/semver/ch.fuzzle.gradle.semver.gradle.plugin/maven-metadata.xml.svg?label=gradlePluginPortal)](https://plugins.gradle.org/plugin/ch.fuzzle.gradle.semver)
[![codecov](https://codecov.io/gh/f-u-z-z-l-e/semver-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/f-u-z-z-l-e/semver-plugin)

## Summary
This gradle plugin sets the project version of your project based on your projects
[git tags](https://git-scm.com/book/en/v2/Git-Basics-Tagging) according to the Semantic Versioning
[specification](https://semver.org). It works out of the box, but also offers some sophisticated configuration options.

## Versioning Tasks
The following task are added to gradle:
* nextVersion
  * Calculates the project version according to semantic versioning. See [version evaluation](#version-evaluation).

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

## Configuration
Refer to [plugins.gradle.org](https://plugins.gradle.org/plugin/ch.fuzzle.gradle.semver) on how to apply the plugin.

### Groovy
```groovy
semver {
    prefix = 'v' // sets the version prefix. -> v0.0.1
    preRelease = 'rc1' // sets the pre-release string (default == git branch name)
    releaseBranch = 'master' // sets the git branch name that is considered to be the 'release' branch.
    tagMessage = 'Tagged automatically.' // sets the message the tagHeadCommit task should use (default = 'Tagged by 'ch.fuzzle.gradle.semver' gradle plugin.')
}
```

### Kotlin
```kotlin
semver {
    prefix.value( "v") // sets the version prefix. -> v0.0.1
    preRelease.value("rc1") // sets the pre-release string (default == git branch name)
    releaseBranch.value("master") // sets the git branch name that is considered to be the 'release' branch.
    tagMessage.value("Tagged automatically.") // sets the message the tagHeadCommit task should use (default = 'Tagged by 'ch.fuzzle.gradle.semver' gradle plugin.')
}
```