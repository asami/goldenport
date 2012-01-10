seq(giter8Settings :_*)

name := "goldenport"

version := "0.3.0-SNAPSHOT"

organization := "org.goldenport"

scalaVersion := "2.9.1"

// crossScalaVersions := Seq("2.9.0")

// retrieveManaged := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

resolvers += "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

resolvers += Classpaths.typesafeResolver

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.9.1"

libraryDependencies += "org.scalatest" % "scalatest_2.9.1" % "1.6.1" % "test"

libraryDependencies += "junit" % "junit" % "4.7" % "test"

libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"

libraryDependencies += "commons-fileupload" % "commons-fileupload" % "1.2.2" % "provided"

libraryDependencies += "commons-io" % "commons-io" % "1.3.2" % "provided"

libraryDependencies += "org.apache.poi" % "poi" % "3.8-beta4" % "provided"

libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.8-beta4" % "provided"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.3"

publishTo := Some(Resolver.file("asamioffice", file("target/maven-repository")))
