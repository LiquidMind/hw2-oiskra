package AnalysisEngines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;

import com.aliasi.chunk.NBestChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.lti.oaqa.bio.annotate.entrezgene_wrapper.EntrezGeneWrapper;
import edu.cmu.lti.oaqa.bio.resource_wrapper.Entity;
import edu.cmu.lti.oaqa.bio.resource_wrapper.Term;
import UIMATypes.*;

//import edu.cmu.lti.oaqa.bio.annotate.entrezgene_wrapper.*;

/**
 * Looks for the entity in Entrez Gene database
 * Save item to pos_gene.db in case if entity is a gene mention and to neg_gene.db otherwise 
 * 
 * @author <a href="mailto:oleg.iskra@gmail.com">Oleg Iskra</a>  
 */

public class EntrezGeneAnnotator extends JCasAnnotator_ImplBase {

  static EntrezGeneWrapper egw;
  static HashMap<String, Boolean> cache;
  static String filePathString = "cache.db";
  static int counter = 0;
  
  @Override
  public void initialize(UimaContext aUimaContext)
  {
    if (egw == null) {
      try {
        egw = new EntrezGeneWrapper();
        System.out.println("Created EntrezGeneWrapper");
      } catch (Exception e) {
        throw new UIMARuntimeException(e);
      }
    }
    
    if (cache == null) {
      File file = new File(filePathString);
      if (file.exists() && file.isFile()) {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream; 
        try {
          fileInputStream = new FileInputStream(file);
          objectInputStream = new ObjectInputStream(fileInputStream);
          cache = (HashMap<String, Boolean>) objectInputStream.readObject();
          fileInputStream.close();
        } catch (FileNotFoundException e) {
          new RuntimeException(e);
        } catch (IOException e) {
          new RuntimeException(e);
        } catch (ClassNotFoundException e) {
          new RuntimeException(e);
        }
      } else {
        cache = new HashMap<String, Boolean>();
      }
    }
  }
    
  public static void saveCache() {
    //System.out.println("saveCache of EntrezGeneAnnotator");
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(filePathString, false);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(cache);
      fileOutputStream.close();
    } catch (FileNotFoundException e) {
      new RuntimeException(e);
    } catch (IOException e) {
      new RuntimeException(e);
    }
  }
  
  @Override
  public void process( JCas aJCas ) throws AnalysisEngineProcessException {
    Term term;
    
    double oldConfidence;
    double newConfidence;

    String text = aJCas.getDocumentText();

    String sentenceId = "empty";
    FSIterator<TOP> fsit = aJCas.getJFSIndexRepository().getAllIndexedFS(UIMATypes.Sentence.type);

    if (fsit.hasNext()) {
      UIMATypes.Sentence sentence = (UIMATypes.Sentence) fsit.next();
      sentenceId = sentence.getId();
      // System.out.println(sentence);
    }
    
    FSIterator<Annotation> genes = aJCas.getAnnotationIndex(Gene.type).iterator();
    HashSet<Annotation> toRemove = new HashSet<Annotation>(); 

    for (int i = 0; genes.hasNext() && counter < 25000;) {
      if (counter % 50 == 0) EntrezGeneAnnotator.saveCache();
      
      Gene gene = (Gene) genes.next();
      //System.out.println(gene);
      
      System.out.println("PID: " + gene.getCasProcessorId() + ", SID: " + sentenceId + " [" + gene.getBegin() + ", " + gene.getEnd() + "]" + 
        ", text: " + gene.getText() + ", conf: " + gene.getConfidence());

      ArrayList<String> searchResults = null;

      if (gene.getConfidence() == -10000) {
        System.out.println(counter++);
        
        // Search for this gene text in local cache
        if (cache.containsKey(gene.getText())) {
          System.out.println(gene.getText() + ": " + cache.get(gene.getText()));
        } else {
          // Search Entrez Gene Database
          term = egw.getTerm(gene.getText());
          System.out.println(term);
          if (term != null) {
            System.out.println("Term: " + term.getTerm() + ", hash: " + term.hashCode());
            cache.put(gene.getText(), true);
          } else {
            cache.put(gene.getText(), false);
          }
        }
        
        if (cache.get(gene.getText())) {
          oldConfidence = gene.getConfidence();
          newConfidence = -Math.sqrt(-oldConfidence); 
          gene.setConfidence(newConfidence);
          System.out.println("EntrezGene >>> Confidence was changed from " + oldConfidence + " to " + newConfidence);         
        } else {
          // We cann't remove it immidiately due to strange UIMA behaviour
          toRemove.add(gene);
        }
      }
    }
    
    // Remove all marked annotations from indexes
    for (Annotation annotation : toRemove) {
      annotation.removeFromIndexes();
    }
  }
}
