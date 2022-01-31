package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF obj=new CRF(64);
    MerkleTree trq=new MerkleTree();
    if(tB.dgst.substring(0,4).equals("0000")==false){
      return false;
    }
    if(tB.previous==null){
      if(!tB.dgst.equals(obj.Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce))){
        return false;
      }
      
      if(tB.trsummary.equals(trq.Build(tB.trarray))==false){
        return false;
      }
      return true;
    }
    else{
      if(!tB.dgst.equals(obj.Fn(tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce))){
        return false;
      }
      if(tB.trsummary.equals(trq.Build(tB.trarray))==false){
        return false;
      }
      for(int i=0; i<tB.trarray.length ; i++){
        if(tB.previous.checkTransaction(tB.trarray[i])==false){
          return false;
        }
      }
      return true;
    }

  }

  public TransactionBlock FindLongestValidChain () {
    TransactionBlock k=null,tba,tbk;
    int maxi=0,count=0;
    for(int i=0;i<lastBlocksList.length;i++){
      tba=null;
      tbk=lastBlocksList[i];
      while(tbk!=null){
        if(checkTransactionBlock(tbk)){
          if(count==0){
            tba=tbk;
            count+=1;
          }
          else{
            count+=1;
          }

        }
        else{
          count=0;
          tba=null;
        }
        tbk=tbk.previous;

      }
      if(maxi<count){
        maxi=count;
        k=tba;
      }
      count=0;
    }
    return k;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock=this.FindLongestValidChain();
    long x=1000000001L;
    newBlock.nonce="1000000001";
    String pdgst;
    CRF obj=new CRF(64);
    if(lastBlock!=null){pdgst=lastBlock.dgst;}
    else{pdgst=start_string;}
    newBlock.dgst=obj.Fn(pdgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    while(!newBlock.dgst.substring(0,4).equals("0000")){
      x++;
      newBlock.nonce=String.valueOf(x);
      newBlock.dgst=obj.Fn(pdgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    }
  
    boolean flag=false;
    for(int i=0;i<lastBlocksList.length;i++){
      if(lastBlocksList[i]==lastBlock){
        lastBlocksList[i]=newBlock;
        flag=true;
        break;
      }
    }
    
    if(!flag){
      for(int i=0;i<lastBlocksList.length;i++){
        if(lastBlocksList[i]==null){
          lastBlocksList[i]=newBlock;
        }
      }
    }
    newBlock.previous=lastBlock;
  }
}
