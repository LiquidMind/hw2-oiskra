package CASConsumers;

import java.util.Set;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceProcessException;

import UIMATypes.*;
import SharedResources.*;

/**
 * Evaluate precision, recall and F1 measure using file with gold samples
 * @author <a href="mailto:oleg.iskra@gmail.com">Oleg Iskra</a>
 */
public class EvaluationConsumer extends CasConsumer_ImplBase {

  private int fp; // false positive
  private int fn; // false negative
  private int tp; // true positive

  @Override
  public void initialize() {
    fp = fn = tp = 0;
  }

  @Override
  public void destroy() {
    // Compute precision, recall and F1 measure
    double precision = tp / ((double) tp + fp) * 100.0;
    double recall = tp / ((double) tp + fn) * 100.0;
    double f1 = 2 * precision * recall / (precision + recall);

    // Print evaluation data to console
    System.out.println("========================================");
    System.out.println(String.format("Precision: %.2f", precision));
    System.out.println(String.format("Recall: %.2f", recall));
    System.out.println(String.format("F1: %.2f", f1));
    System.out.println("========================================");
  }

  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    try {
      GoldenSampleMap samples = (GoldenSampleMap) getUimaContext().getResourceObject("samples");
      JCas aJCas;
      try {
        aJCas = aCAS.getJCas();
      } catch (CASException e) {
        throw new AnalysisEngineProcessException(e);
      }

      String sentenceId = "none";
      FSIterator<TOP> it = aJCas.getJFSIndexRepository().getAllIndexedFS(UIMATypes.Sentence.type);

      if (it.hasNext()) {
        UIMATypes.Sentence sentence = (UIMATypes.Sentence) it.next();
        sentenceId = sentence.getId();
      }

      Set<String> goldenSamples = samples.getSentenceAnnotations(sentenceId);

      for (Annotation current : aJCas.getAnnotationIndex(Gene.type)) {
        Gene gene = (Gene) current;

        // System.out.println(gene.getText());

        // If annotation is the golden sample increase true positive counter and remove gene from
        // the GoldenSample set
        // in other case increase false positive counter
        String line = sentenceId + "|" + gene.getBegin() + " " + gene.getEnd() + "|"
                + gene.getText(); // gene.getText();

        if (goldenSamples != null && goldenSamples.contains(line)) {
          tp++;
          goldenSamples.remove(line);
          // System.out.println("tp++");
        } else {
          fp++;
          // System.out.println("fp++");
        }
      }
      // Increase false negative counter on the amount of left annotations
      if (goldenSamples != null) {
        fn += goldenSamples.size();
        // System.out.println("fn++");
      }

    } catch (ResourceAccessException e) {
      throw new UIMARuntimeException(e);
    }

  }

}
