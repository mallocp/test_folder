import org.apache.spark.ml.classification.{BinaryLogisticRegressionSummary, LogisticRegression}
import org.apache.spark.sql.SparkSession

class regressionLogis(spark: SparkSession) {
  // Instanciation algorithme regression logistic
  val lr = new LogisticRegression()
      //lr.setMaxIter(10)
      //.setRegParam(0.01)

  //Instanciation de la classe readFile pour la transformation d'un fichier texte au format json
  val get = new readFile()
  //Instanciation de la class countWords, pour la conversion base test et apprentissage en vecteur de frequences de mots
  val countwords = new countWords()
  // Renvoie les données un dataframe de frequence de mots
  var data =  countwords.countW(get.Dataframe(spark))
  // Obtention base de test, base d'apprentissage
  var datasplit = new inputData()
  // lancement de l'apprentissage pour la creation du modèle de prédiction
  var lrModel =  lr.fit(datasplit.inputdata(data)(0))
  val summary = lrModel.summary
  val bSummary = summary.asInstanceOf[BinaryLogisticRegressionSummary]
  val training = lrModel.transform(datasplit.inputdata(data)(0))
  // Projection des données base de test sur le modèle d'apprentissage
  val test = lrModel.transform(datasplit.inputdata(data)(1))
  // resultat de la courbe de ROC
  def areaUnderRoc = println("Air Courbe de ROC RLogistique:", bSummary.areaUnderROC)
 // Nombre de labels en entrée pour la base de test
  def nombreLabelTest = println("Nombre des labels tests en entrées RLogistique:", test.select("label").count)
  // Nombre de labels prédits pour la base de test
  def predictionlabel =  println("Nombre des labels tests predits RLogistique:", test.filter("label == prediction").count)



}
