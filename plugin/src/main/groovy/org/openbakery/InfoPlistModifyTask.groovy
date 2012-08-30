package org.openbakery

import org.gradle.api.tasks.TaskAction


class InfoPlistModifyTask extends AbstractXcodeTask {

	@TaskAction
	def prepare() {
		def infoPlist = getInfoPlist()
		println "Updating " + infoPlist


		if (project.infoplist.bundleIdentifier != null) {
			runCommand([
							"/usr/libexec/PlistBuddy",
							infoPlist,
							"-c",
							"Set :CFBundleIdentifier " + project.infoplist.bundleIdentifier
			])
		}

		println "project.infoplist.version: " + project.infoplist.version
		def version;
		if (project.infoplist.version != null) {
			version = project.infoplist.version
		} else {
			version = runCommandWithResult([
							"/usr/libexec/PlistBuddy",
							infoPlist,
							"-c",
							"Print :CFBundleVersion"])
		}

		if (project.infoplist.versionSuffix) {
			version = version + project.infoplist.versionSuffix
		}

		if (project.infoplist.versionPrefix) {
			version = project.infoplist.versionPrefix + version
		}

		def shortVersionString

		if (project.infoplist.shortVersionString != null) {
			shortVersionString = project.infoplist.shortVersionString
		} else {
			shortVersionString = runCommandWithResult([
							"/usr/libexec/PlistBuddy",
							infoPlist,
							"-c",
							"Print :CFBundleShortVersionString"])
		}

		if (project.infoplist.shortVersionStringSuffix) {
			shortVersionString = shortVersionString + project.infoplist.shortVersionStringSuffix
		}

		if (project.infoplist.shortVersionStringPrefix) {
			shortVersionString = project.infoplist.shortVersionStringPrefix + shortVersionString
		}


		println "Modify CFBundleVersion to " + version

		runCommand([
						"/usr/libexec/PlistBuddy",
						infoPlist,
						"-c",
						"Set :CFBundleVersion " + version])

		println "Modify CFBundleShortVersionString to " + shortVersionString
		runCommand([
						"/usr/libexec/PlistBuddy",
						infoPlist,
						"-c",
						"Set :CFBundleShortVersionString " + shortVersionString])

	}

}