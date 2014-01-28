import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "LineBattle"

version := "1.0"

scalaVersion := "2.10.2"

mainClass := Some("TextualUI")

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "org.scalafx" %% "scalafx" % "1.0.0-M3"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10+"

// Add dependency on JavaFX library based on JAVA_HOME variable
unmanagedJars in Compile += Attributed.blank(file("lib/jfxrt.jar"))

jacoco.settings