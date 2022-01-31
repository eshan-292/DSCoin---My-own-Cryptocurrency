package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    CRF en=new CRF(64);
    String pdgst;
    newBlock.nonce="1000000001";
    long x=1000000001L;
    if(lastBlock!=null){
      pdgst=lastBlock.dgst;
    }
    else{
      pdgst=start_string;
    }
    newBlock.dgst=en.Fn(pdgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    while(!newBlock.dgst.startsWith("0000")){
      x++ ;
      newBlock.nonce=String.valueOf(x);
      newBlock.dgst=en.Fn(pdgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    }
    newBlock.previous=lastBlock;
    lastBlock=newBlock;
  }
}
