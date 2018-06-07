import java.io._
import edu.stanford.nlp.process.Morphology
import org.apache.spark.sql.{DataFrame, SparkSession}
import scala.io.Source
import org.apache.spark.sql.functions._
import edu.stanford.nlp.simple.Document
import scala.collection.JavaConversions._

class readFile {

  def Dataframe(spark: SparkSession):(DataFrame) ={
    //Récupération des repertoires de fichiers des avis positifs et negatifs
    val negRepFile = new File("neg")
    val posRepFile = new File("pos")
    //Récupération des fichiers sous forme de liste
    val ng= negRepFile.listFiles.filter(_.isFile).toList
    val ps= posRepFile.listFiles.filter(_.isFile).toList
    val fn= new FileWriter("data.json")
    createJsonFile(ng,ps,fn)

    // conversion en dataframe du fichier data.json
    var df=spark.read.format("json").load("data.json")
    df=df.drop(col("_corrupt_record"))
    df // renvoie le dataframes df
}
  // converti un fichier text en format json
  def createJsonFile(ng:List[File],ps:List[File],fn:FileWriter) {

    for(lines <-ng) { // ng pour les contenus de label 0

      var line = Source.fromFile(lines.getPath).getLines


      for(x<-line) {
        var l= x.replaceAll("""[! @ # $ % ^ & * ( ) _ + - − , " ' ; : . ` ? -- ]""", " ")
        var s = lines.getName.split("_")(1)
        s = s.replaceAll(".txt", " ")
        s = s.trim()
        val morphology = new Morphology()
        // Ecriture des contenus de label 0 dans le fichier data.json
        fn.write("{'sentence':'"+getLemmaText(addslashes(l),morphology)+"'"+",'label':"+0+"}\n")
      }

    }

    for(lines <-ps) { // ps pour les contenus de label 1
      var line = Source.fromFile(lines.getPath).getLines

      for(x<-line) {
        var l= x.replaceAll("""[! @ # $ % ^ & * ( ) _ + - − , " ' ; : . ` ? -- ]""", " ")
        var s = lines.getName.split("_")(1)
        s = s.replaceAll(".txt", " ")
        s = s.trim()
        val morphology = new Morphology()
        // Ecriture des contenus de label 1 dans le fichier data.json
       fn.write("{'sentence':'"+getLemmaText(addslashes(l),morphology)+"'"+",'label':"+1+"}\n")
      }

    }

  }

  // conversion d'un contenu texte en chaine de caractères
  def addslashes(str: String): String = {
    val sb = new StringBuilder
    str.foreach {
      case '\0' => sb.append("\\0")
      case '\'' => sb.append("\\'")
      case '"' => sb.append("\\\"")
      case '\\' => sb.append("\\\\")
      case ch => sb.append(ch)
    }
    sb.toString()
  }

  // lemmatisation des mots, choix d'une seule forme de mot
  def getLemmaText(document: String, morphology: Morphology):String ={
    val string = new StringBuilder()
    val value = new Document(document).sentences().toList.flatMap { a =>
      val words = a.words().toList
      val tags = a.posTags().toList
      (words zip tags).toMap.map { a =>
        val newWord = morphology.lemma(a._1, a._2)
        val addedWoed = if (newWord.length >1) {
          newWord    } else { "" }
        string.append(addedWoed + " ")
      }
    }

    string.toString()
  }




}


