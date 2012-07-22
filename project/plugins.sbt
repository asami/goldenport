resolvers += Classpaths.typesafeResolver

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/groups/scala-tools/"

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0-RC1")

addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.0.10")

addSbtPlugin("net.databinder" %% "giter8-plugin" % "0.3.2")
