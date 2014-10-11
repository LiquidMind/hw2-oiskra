package AnalysisEngines;

import java.io.File;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;

import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.ScoredObject;
import com.aliasi.chunk.*;

import UIMATypes.*;

/**
 * Annotator based on LingPipe models
 * The most accurate model is GeneTag
 * http://alias-i.com/lingpipe/web/models.html
 * @author <a href="mailto:oleg.iskra@gmail.com">Oleg Iskra</a>
 */
public class LingPipeAnnotator extends JCasAnnotator_ImplBase {

  // Instance of LingPipe NBestChunker for common use
  private NBestChunker chunker;
  
	@Override
	public void initialize(UimaContext aUimaContext)
	{
		try {
			//chunker = (Chunker)AbstractExternalizable.readObject(new File(aUimaContext.getResourceFilePath("LingPipeModel")));
		  chunker = (NBestChunker)AbstractExternalizable.readObject(new File(aUimaContext.getResourceFilePath("LingPipeModel")));
		} catch (Exception e) {
			throw new UIMARuntimeException(e);
		} 
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		String text = aJCas.getSofaDataString();
		//Chunking chunks = chunker.chunk(text);

		Iterator<ScoredObject<Chunking>> it = chunker.nBest(text.toCharArray(), 0, text.length(), 1);
		ScoredObject<Chunking> chunks = it.next();
    double confidence = chunks.score();
    
    for (Chunk chunk : chunks.getObject().chunkSet())
		{
		  // Skip whitespaces to correctly specify start and end offsets before saving annotation
      int bgn = text.substring(0, chunk.start()).replaceAll("\\s", "").length(); 
			int end = text.substring(0, chunk.end()).replaceAll("\\s", "").length() - 1; 
			
		  // if (bgn != entry.getKey()) System.out.println(entry.getKey() - bgn);
      // if (end != entry.getValue()) System.out.println(entry.getValue() - end);
			
      // Add new finding to index
      Gene gene = new Gene(aJCas, bgn, end);
      gene.setText(text.substring(chunk.start(), chunk.end()).trim());
      gene.setCasProcessorId(this.getClass().getName());
      gene.setConfidence(confidence);
      gene.addToIndexes();
      
      //System.out.println(gene);
 
      /*
      if (confidence > -100 || confidence < -1000) {
        System.out.println("Confidence: " + confidence);
        System.out.println(text.substring(chunk.start(), chunk.end()).trim());
      }
      */
      
      //System.out.println(gene);
		}
	}
	

}
