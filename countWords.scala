import org.apache.spark.ml.feature.{CountVectorizer,StopWordsRemover, Tokenizer}
import org.apache.spark.sql.{DataFrame}

class countWords { // Pour la transformation du contenu textuel en frequence de vecteur de mots


  def countW(df:DataFrame):DataFrame = // Transformation des données en frequence de mots
  {
    // Elimination des phrases nulles puis coupure des phrases en sequence de mots
    var tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    var wordsDF = tokenizer.transform(df.na.drop(Array("sentence","label")))

    // Ensemble de vecteurs de mots
    var remover = new StopWordsRemover().setInputCol("words").setOutputCol("filteredWords")
    var noStopWordsDF = remover.transform(wordsDF)

    // Ensemble de vecteurs de frequence de mots
    var countVectorizer = new CountVectorizer().setInputCol("filteredWords").setOutputCol("features")
    var countVectorizerM = countVectorizer.fit(noStopWordsDF)
    var countVectorizerDF = countVectorizerM.transform(noStopWordsDF)

    // renvoie un dataframe de frequence de mots
    countVectorizerDF


  }



}
