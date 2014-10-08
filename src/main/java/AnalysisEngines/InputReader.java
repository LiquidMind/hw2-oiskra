package AnalysisEngines;

import java.io.*;

import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;

import UIMATypes.*;

/**
 * Simple collection reader that reads input file with sentences
 * 
 * @author <a href="mailto:oleg.iskra@gmail.com">Oleg Iskra</a>
 */
public class InputReader extends CollectionReader_ImplBase {

  // Instance of BufferReader for common use
  private BufferedReader br;

  @Override
  public void initialize() {
    // open the input file
    String path = (String) getConfigParameterValue("InputPath");
    try {
      FileReader fr = new FileReader(path);
      br = new BufferedReader(fr);
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, e.getLocalizedMessage());
      throw new UIMA_IllegalArgumentException();
    }
  }

  @Override
  public void getNext(CAS newcas) throws IOException, CollectionException {
    // get the sentence and ID
    try {
      // split key and set SOFA
      JCas jcas = newcas.getJCas();
      String[] data = br.readLine().split("\\s", 2);
      jcas.setSofaDataString(data[1], "text");

      // annotate with sentence ID
      UIMATypes.Sentence sentence;
      sentence = new UIMATypes.Sentence(newcas.getJCas());
      sentence.setId(data[0]);
      sentence.addToIndexes();
    } catch (CASException e) {
      getLogger().log(Level.SEVERE, e.getLocalizedMessage());
      throw new UIMA_IllegalArgumentException();
    }
  }

  @Override
  public void close() throws IOException {
    // close the data
    br.close();

  }

  @Override
  public Progress[] getProgress() {
    return new Progress[0]; // cannot say progress, otherwise we have to read whole input into file
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return br.ready(); // check if ready
  }

}
