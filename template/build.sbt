import play.PlayImport.PlayKeys._
import scoverage.ScoverageSbtPlugin.ScoverageKeys._

name := "%%NAME%%"

scalaVersion in ThisBuild := "%%SCALA_VERSION%%"

// required because of issue between scoverage & sbt
parallelExecution in Test in ThisBuild := true

lazy val generated = project
  .in(file("generated"))
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq(
      ws
    )
  )

lazy val api = project
  .in(file("api"))
  .dependsOn(generated)
  .aggregate(generated)
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    routesImport += "%%PACKAGE_NAME%%.Bindables._",
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= Seq(
      ws,
      jdbc,
      "com.typesafe.play" %% "anorm" % "2.5.0",
      "org.postgresql" % "postgresql" % "9.4.1207",
      "org.scalatestplus" %% "play" % "%%SCALATESTPLUS_VERSION%%" % "test"
    )
  )

lazy val commonSettings: Seq[Setting[_]] = Seq(
  name <<= name("%%NAME%%-" + _),
  libraryDependencies ++= Seq(
    specs2 % Test,
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  ),
  scalacOptions += "-feature",
  coverageHighlighting := true
)
