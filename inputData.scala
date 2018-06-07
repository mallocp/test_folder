import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.functions._

class inputData {

  def inputdata(df:DataFrame): Array[Dataset[Row]] ={
    // selection des données
    var inputData = df.select("features","label").withColumn("label", col("label").cast("double") )
    // separation de données en base de test et d' apprentissage
    val Array(training, test) = inputData.randomSplit(Array(0.8, 0.2))
    // renvoie un tableau base de test et d'apprentissage
    Array(training, test)
  }

}
