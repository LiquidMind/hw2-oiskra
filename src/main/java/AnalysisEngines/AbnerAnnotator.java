package AnalysisEngines;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.stanford.nlp.util.HasInterval;
import edu.stanford.nlp.util.Interval;
import edu.stanford.nlp.util.IntervalTree;
import UIMATypes.*;
import abner.Tagger;

class IncorrectModelNameException extends RuntimeException {}

/**
 * Use ABNER tagger for annotation with BioCreative or NLPBA model
 * 
 * @author <a href="mailto:oleg.iskra@gmail.com">Oleg Iskra</a>
 */
public class AbnerAnnotator extends JCasAnnotator_ImplBase {

  private Tagger tagger;
  private String mode;
  
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    Integer wMode = Tagger.BIOCREATIVE;

    if (aContext.getConfigParameterValue("abnerModel").equals("BIOCREATIVE"))
      wMode = Tagger.BIOCREATIVE;
    else if (aContext.getConfigParameterValue("abnerModel").equals("NLPBA"))
      wMode = Tagger.NLPBA;
    else
      throw new IncorrectModelNameException();

    tagger = new Tagger(wMode);
    
    switch (tagger.getMode()) {
      case Tagger.BIOCREATIVE:
        mode = "BIOCREATIVE";
        break;
      case Tagger.NLPBA:
        mode = "NLPBA";
        break;
      default:
        mode = "UNDEFINED";
        break;
    }
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    String text = aJCas.getDocumentText();
    Vector<String[][]> sentences = tagger.getSegments(text);

    double oldConfidence;
    double newConfidence;
    Interval<Integer> interval;
    HashMap<Interval<Integer>, String> hashMap = new HashMap<Interval<Integer>, String>();
    
    /*
    IntervalTree<String, Interval<Integer>> intervalTree;
    
    = new IntervalTree<String, Integer>();
    */
    
    // To store indexes of found chunks 
    int bgn = 0;
    int end = 0;
    
    // Go through every sentence
    for (int i = 0; i < sentences.size(); i++) {
      String[][] chunks = sentences.get(i);
      //System.out.println(Arrays.deepToString(chunks));

      // Go through every segment
      for (int j = 0; j < chunks[0].length; j++) {
        String withoutWhiteSpaces = chunks[0][j].replaceAll("\\s", "");
        // If it's recognized entity no matter DNA or PROTEIN
        if (!chunks[1][j].equals("O")) {
          end = bgn + withoutWhiteSpaces.length() - 1;
          hashMap.put(Interval.toInterval(bgn, end), chunks[0][j]);
        }

        bgn += withoutWhiteSpaces.length();
      }
    }
    
    // Get already annotated gene mentions from previous LingPipeAnnotator
    FSIterator<Annotation> genes = aJCas.getAnnotationIndex(Gene.type).iterator();
    
    for (int i = 0; genes.hasNext(); i++) {
      Gene gene = (Gene) genes.next();
      //System.out.println("Gene >>> " + gene);
      interval = Interval.toInterval(gene.getBegin(), gene.getEnd());
      if (hashMap.containsKey(interval)) {
        System.out.println("Abner." + mode + " >>> Interval " + interval + " has already annotated as \"" + gene.getText() + "\" <<<");
        if (hashMap.get(interval).equals(gene.getText())) {
          oldConfidence = gene.getConfidence();
          newConfidence = -Math.sqrt(-oldConfidence); 
          gene.setConfidence(newConfidence);
          System.out.println("Abner." + mode + " >>> Confidence was changed from " + oldConfidence + " to " + newConfidence);
        } else {
          System.out.println(interval + ": " + gene.getText() + " differs from " + hashMap.get(interval));
        }
        hashMap.remove(interval);
      }
    }
    
    // Add all left annotations to UIMA index
    for (Interval<Integer> currentInterval : hashMap.keySet()) {
      Gene newGene = new Gene(aJCas, currentInterval.getBegin(), currentInterval.getEnd());
      newGene.setCasProcessorId(this.getClass().getName() + "." + mode);
      // Approximately reported F1-score between 69.9 and 70.5
      // details are here http://pages.cs.wisc.edu/~bsettles/abner/#performance
      newGene.setConfidence(-10000);
      newGene.setText(hashMap.get(currentInterval));
      newGene.addToIndexes();
    }    
  }
}
