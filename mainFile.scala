import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}



object mainFile {
  Logger.getLogger("org").setLevel(Level.ERROR)

  def main(args:Array[String]){
    // Instanciation SparkSession
    val spark= SparkSession.builder().master("local[*]").appName("Analysis").config("spark.sql.warehouse.dir","C:/data/").getOrCreate()
    // Instanciation Regression logistique
    val RegressionLogistique = new regressionLogis(spark)
    RegressionLogistique.areaUnderRoc
    RegressionLogistique.nombreLabelTest
    RegressionLogistique.predictionlabel


  }

}
