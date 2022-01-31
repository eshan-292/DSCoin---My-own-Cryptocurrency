package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    MerkleTree mtr=new MerkleTree();
    Transaction[] tarr=new Transaction[t.length];
    for(int i=0;i<t.length;i++){
      tarr[i]=t[i];
    }
    trarray=tarr;
    trsummary=mtr.Build(tarr);
    Tree=mtr;
  }

  public boolean checkTransaction (Transaction t) {
    TransactionBlock tb=t.coinsrc_block;
    TransactionBlock curr=this;
    int k=-1;
    if(t.coinsrc_block==null){
      return true;
    }
    for(int i=0;i<tb.trarray.length;i++){
      if(t.coinID.equals(tb.trarray[i].coinID) && t.Source.UID.equals(tb.trarray[i].Destination.UID)){
        k=i;
        break;
      }
    }
    if(k==-1){
      return false;
    }
    for(int i=k+1;i<tb.trarray.length;i++){
      if(t.coinID.equals(tb.trarray[i].coinID) ){
        return false;
      }
    }
    while(curr!=tb){
      for(int i=0;i<curr.trarray.length;i++){
        if(t.coinID.equals(curr.trarray[i].coinID)){
          return false;
        }
      }
      curr=curr.previous;
    }
    
    return true;
    
  }
}
