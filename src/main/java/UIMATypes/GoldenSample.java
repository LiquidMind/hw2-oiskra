

/* First created by JCasGen Tue Oct 07 22:40:58 EDT 2014 */
package UIMATypes;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import edu.cmu.deiis.types.Annotation;


/** The golden sample to evaluate quality of annotators
 * Updated by JCasGen Tue Oct 07 22:40:58 EDT 2014
 * XML source: D:/Projects/GitHub/hw2-oiskra/src/main/resources/descriptors/typeSystemDescriptor.xml
 * @generated */
public class GoldenSample extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(GoldenSample.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected GoldenSample() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public GoldenSample(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public GoldenSample(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public GoldenSample(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: sentenceId

  /** getter for sentenceId - gets The id of the sentence that goldene gene name comes from
   * @generated
   * @return value of the feature 
   */
  public String getSentenceId() {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_sentenceId == null)
      jcasType.jcas.throwFeatMissing("sentenceId", "UIMATypes.GoldenSample");
    return jcasType.ll_cas.ll_getStringValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_sentenceId);}
    
  /** setter for sentenceId - sets The id of the sentence that goldene gene name comes from 
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentenceId(String v) {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_sentenceId == null)
      jcasType.jcas.throwFeatMissing("sentenceId", "UIMATypes.GoldenSample");
    jcasType.ll_cas.ll_setStringValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_sentenceId, v);}    
   
    
  //*--------------*
  //* Feature: begin

  /** getter for begin - gets Begin of the gene chunk in sentence
   * @generated
   * @return value of the feature 
   */
  public int getBegin() {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_begin == null)
      jcasType.jcas.throwFeatMissing("begin", "UIMATypes.GoldenSample");
    return jcasType.ll_cas.ll_getIntValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_begin);}
    
  /** setter for begin - sets Begin of the gene chunk in sentence 
   * @generated
   * @param v value to set into the feature 
   */
  public void setBegin(int v) {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_begin == null)
      jcasType.jcas.throwFeatMissing("begin", "UIMATypes.GoldenSample");
    jcasType.ll_cas.ll_setIntValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_begin, v);}    
   
    
  //*--------------*
  //* Feature: end

  /** getter for end - gets End of the gene chunk in sentence
   * @generated
   * @return value of the feature 
   */
  public int getEnd() {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "UIMATypes.GoldenSample");
    return jcasType.ll_cas.ll_getIntValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_end);}
    
  /** setter for end - sets End of the gene chunk in sentence 
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnd(int v) {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "UIMATypes.GoldenSample");
    jcasType.ll_cas.ll_setIntValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_end, v);}    
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets Text of goldene gene sample
   * @generated
   * @return value of the feature 
   */
  public String getText() {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "UIMATypes.GoldenSample");
    return jcasType.ll_cas.ll_getStringValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets Text of goldene gene sample 
   * @generated
   * @param v value to set into the feature 
   */
  public void setText(String v) {
    if (GoldenSample_Type.featOkTst && ((GoldenSample_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "UIMATypes.GoldenSample");
    jcasType.ll_cas.ll_setStringValue(addr, ((GoldenSample_Type)jcasType).casFeatCode_text, v);}    
  }

    