name := """studentPortal"""
organization := "com.studentPortal"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.9"
//resolvers += "typesafe" at "https://repo.typesafe.com/typesafe/releases/"
//libraryDependencies += guice

libraryDependencies ++= Seq(
  //jdbc,
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "com.h2database" % "h2" % "1.4.199" 
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.studentPortal.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.studentPortal.binders._"
