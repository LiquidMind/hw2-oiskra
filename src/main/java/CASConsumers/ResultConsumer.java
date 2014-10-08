package CASConsumers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.examples.tokenizer.Sentence;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;

import UIMATypes.*;

/**
 * Writes the CAS to the prescribed output format
 * "sentenceId|begin end|gene"
 * @author <a href="mailto:oleg.iskra@gmail.com">Oleg Iskra</a>
 *
 */
public class ResultConsumer extends CasConsumer_ImplBase {

  // Instance of BufferedWriter for common use
  private BufferedWriter bw;

  @Override
  public void initialize() {
    // open the output file
    String path = (String) getConfigParameterValue("OutputPath");
    try {
      FileWriter fw = new FileWriter(path);
      bw = new BufferedWriter(fw);
    } catch (Exception e) {
      throw new UIMA_IllegalArgumentException(e);
    }
  }

  @Override
  public void destroy() {
    try {
      bw.close();
    } catch (IOException e) {
    }
  }

  @Override
  public void processCas(CAS cas) throws AnalysisEngineProcessException {
    JCas aJCas;
    try {
      aJCas = cas.getJCas();
    } catch (CASException e) {
      throw new AnalysisEngineProcessException(e);
    }

    String sentenceId = "empty";
    FSIterator<TOP> it = aJCas.getJFSIndexRepository().getAllIndexedFS(UIMATypes.Sentence.type);

    if (it.hasNext()) {
      UIMATypes.Sentence sentence = (UIMATypes.Sentence) it.next();
      sentenceId = sentence.getId();
      // System.out.println(sentence);
    }

    for (Annotation current : aJCas.getAnnotationIndex(Gene.type)) {
      Gene gene = (Gene) current;
      String line = sentenceId + "|" + gene.getBegin() + " " + gene.getEnd() + "|" + gene.getText() + "\n";
      // Write result to output file
      try {
        bw.write(line);
        bw.flush();
      } catch (IOException e) {
        throw new UIMARuntimeException(e);
      }
    }
  }
}
