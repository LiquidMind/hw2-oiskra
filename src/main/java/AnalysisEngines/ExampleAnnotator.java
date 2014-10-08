package AnalysisEngines;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import UIMATypes.*;

/**
 * Example Annotator based on PosTagNamedEntityRecognizer
 *
 */
public class ExampleAnnotator extends JCasAnnotator_ImplBase {

  private Object recognizer;

  private Method getGeneSpans;

  @Override
  public void initialize(UimaContext aContext) {
    try {
      // Creating object instance from the class name
      Class<?> _class = Class.forName("PosTagNamedEntityRecognizer");
      Constructor<?> _constructor = _class.getConstructor();
      recognizer = _constructor.newInstance();
      // Obtain getGeneSpans method
      getGeneSpans = _class.getMethod("getGeneSpans", String.class);
    } catch (Exception e) {
      throw new UIMARuntimeException(e);
    }
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    try {
      // Using getGeneSpans method to detect all relevant chunks
      @SuppressWarnings("unchecked")
      Map<Integer, Integer> result = (Map<Integer, Integer>) getGeneSpans.invoke(recognizer,
              aJCas.getSofaDataString());
      String text = aJCas.getSofaDataString();

      // Process every found gene chunk
      for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
        // Skip whitespaces to correctly specify start and end offsets before saving annotation
        int bgn, end;
        bgn = text.substring(0, entry.getKey()).replaceAll("\\s", "").length();
        end = text.substring(0, entry.getValue()).replaceAll("\\s", "").length() - 1;

        // if (bgn != entry.getKey()) System.out.println(entry.getKey() - bgn);
        // if (end != entry.getValue()) System.out.println(entry.getValue() - end);
        
        // Add new finding to index
        Gene gene = new Gene(aJCas, bgn, end);
        gene.setText(text.substring(entry.getKey(), entry.getValue()).trim());
        gene.addToIndexes();
        
        //System.out.println(gene);
      }
    } catch (Exception e) {
      throw new UIMARuntimeException(e);
    }
  }
}
