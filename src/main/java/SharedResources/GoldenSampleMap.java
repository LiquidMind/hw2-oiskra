package SharedResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Loads shared golden sample data from the golden sample source file
 * @author <a href="mailto:oleg.iskra@gmail.com">Oleg Iskra</a>
 */
public class GoldenSampleMap implements SharedResourceObject, IGoldenSampleMap {

  private HashMap<String, Set<String>> samples = new HashMap<String, Set<String>>();

  @Override
  public void load(DataResource aData) throws ResourceInitializationException {

    try { // open file
      BufferedReader br = new BufferedReader(new InputStreamReader(aData.getInputStream()));
      String line = br.readLine();

      while (line != null) {
        line = line.trim();

        // Get sentence id and text from the annotation
        String[] chunks = line.split("\\|");
        String sentenceId = chunks[0].trim();
        String text = chunks[2].trim();
        
        //System.out.println(Arrays.toString(chunks));
        
        // Get begin and end offsets
        /* chunks = chunks[1].split(" ");
        Integer bgn = new Integer(chunks[0].trim());
        Integer end = new Integer(chunks[1].trim()); */  
        
        // If it is new sentenceId, create empty set
        if (!samples.containsKey(sentenceId)) {
          samples.put(sentenceId, new HashSet<String>());
        }
        // Add line with gene mention to set
        samples.get(sentenceId).add(line); //samples.get(sentenceId).add(text);

        line = br.readLine();
      }
      br.close();

    } catch (IOException e) {
      throw new UIMARuntimeException(e);
    }

  }

  @Override
  public Set<String> getSentenceAnnotations(String sentenceId) {
    return samples.get(sentenceId);
  }

}
