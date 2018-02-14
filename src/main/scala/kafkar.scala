import com.twitter.chill.KryoSerializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkContext
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SQLContext


import scala.collection.mutable
import org.elasticsearch.spark.streaming._
import org.elasticsearch.spark.sql._



object kafkar {
  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    println("program started")

    val conf = new SparkConf().setMaster("local[3]").setAppName("kafkar").set("spark.testing.memory","2147480000")
      .set("spark serializer", KryoSerializer.getClass.getName)
      .set("es.nodes", "10.2.2.230:9200")
      .set("es.index.auto.create", "true")

    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(8))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark-streaming-consumer-group",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    // my kafka topic name is 'test'
    //returns DStream of (Kafka message key, Kafka message value)
      val kafkaStream = KafkaUtils.createStream(ssc, "localhost:2181","spark-streaming-consumer-group", Map("test3" -> 5))

    kafkaStream.foreachRDD{ rdd=>
        val sqlContext =  SQLContext.getOrCreate(SparkContext.getOrCreate())
        val finalstream = sqlContext.createDataFrame(rdd)
        finalstream.show()
        finalstream.saveToEs("sparkwordcount/docs")

    }

    //val rdd = sc.makeRDD(Seq(kafkaStream))

    //val microbatches = mutable.Queue(rdd)
    //ssc.queueStream(microbatches).saveToEs("spark/docs")
    //kafkaStream.print()
    ssc.start
    ssc.awaitTermination()

  }

}
