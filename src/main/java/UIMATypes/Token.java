

/* First created by JCasGen Tue Oct 07 22:40:58 EDT 2014 */
package UIMATypes;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Different types of tokens
 * Updated by JCasGen Tue Oct 07 22:40:58 EDT 2014
 * XML source: D:/Projects/GitHub/hw2-oiskra/src/main/resources/descriptors/typeSystemDescriptor.xml
 * @generated */
public class Token extends edu.cmu.deiis.types.Token {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Token.class);
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
  protected Token() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Token(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Token(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Token(JCas jcas, int begin, int end) {
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
  //* Feature: WordForm

  /** getter for WordForm - gets The word form of token
   * @generated
   * @return value of the feature 
   */
  public String getWordForm() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_WordForm == null)
      jcasType.jcas.throwFeatMissing("WordForm", "UIMATypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_WordForm);}
    
  /** setter for WordForm - sets The word form of token 
   * @generated
   * @param v value to set into the feature 
   */
  public void setWordForm(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_WordForm == null)
      jcasType.jcas.throwFeatMissing("WordForm", "UIMATypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_WordForm, v);}    
   
    
  //*--------------*
  //* Feature: PartOfSpeech

  /** getter for PartOfSpeech - gets Token's part of speech
   * @generated
   * @return value of the feature 
   */
  public String getPartOfSpeech() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_PartOfSpeech == null)
      jcasType.jcas.throwFeatMissing("PartOfSpeech", "UIMATypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_PartOfSpeech);}
    
  /** setter for PartOfSpeech - sets Token's part of speech 
   * @generated
   * @param v value to set into the feature 
   */
  public void setPartOfSpeech(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_PartOfSpeech == null)
      jcasType.jcas.throwFeatMissing("PartOfSpeech", "UIMATypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_PartOfSpeech, v);}    
  }

    