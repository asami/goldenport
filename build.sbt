// seq(giter8Settings :_*)

name := "goldenport"

version := "0.4.4"

organization := "org.goldenport"

// scalaVersion := "2.9.2"

crossScalaVersions := Seq("2.9.2", "2.9.1")

// retrieveManaged := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

// resolvers += "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

// resolvers += Classpaths.typesafeResolver

// resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

resolvers += "Asami Maven Repository" at "http://www.asamioffice.com/maven"

libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-compiler" % _ }

libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ }

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"

libraryDependencies += "commons-fileupload" % "commons-fileupload" % "1.2.2" % "provided"

libraryDependencies += "commons-io" % "commons-io" % "1.3.2" % "provided"

libraryDependencies += "org.apache.poi" % "poi" % "3.8" % "provided"

libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.8" % "provided"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.4"

libraryDependencies += "org.goldenport" % "goldenport-java-lib" % "0.1.1"

libraryDependencies += "org.goldenport" %% "goldenport-scala-lib" % "0.1.3"

libraryDependencies += "org.goldenport" %% "goldenport-record" % "0.2.0"

libraryDependencies += "org.goldenport" %% "goldenport-swing" % "0.2.0"

libraryDependencies += "org.smartdox" %% "smartdox" % "0.3.0"

publishTo := Some(Resolver.file("asamioffice", file("target/maven-repository")))
