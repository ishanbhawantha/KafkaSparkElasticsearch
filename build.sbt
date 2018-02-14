name := "kafkar"

version := "0.1"

scalaVersion := "2.11.11"

retrieveManaged := true

fork := true

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.2.0"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.1.0"

libraryDependencies += "org.elasticsearch" %% "elasticsearch-spark-20" % "6.1.2"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.0"
