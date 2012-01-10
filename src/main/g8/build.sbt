organization := "$package$"

name := "$sample$"

version := "$version$"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

resolvers += "AsamiOffice Maven Repository" at "http://www.asamioffice.com/maven"

libraryDependencies += "org.goldenport" %% "goldenport" % "0.2.4"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"
