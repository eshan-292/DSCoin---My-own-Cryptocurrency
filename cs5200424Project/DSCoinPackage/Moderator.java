package DSCoinPackage;
import HelperClasses.Pair;
public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    int count=DSObj.bChain.tr_count,temp=0,k=0,latestcoin=99999;
    int n=coinCount/count;
    Members mode=new Members();
    mode.UID="Moderator";
    while(temp<n){
      Transaction[] block=new Transaction[count];
      for(int i=0;i<count;i++){
        Transaction newcoin=new Transaction();
        latestcoin+=1;
        DSObj.latestCoinID=String.valueOf(latestcoin);
        newcoin.coinID=DSObj.latestCoinID;
        if(k==DSObj.memberlist.length){
          k=0;
        }
        newcoin.Destination=DSObj.memberlist[k];
        k+=1;
        newcoin.Source=mode;
        block[i]=newcoin;

      }
      TransactionBlock currblock=new TransactionBlock(block);
      for(Transaction i : block){
        i.Destination.mycoins.add(new Pair<>(i.coinID,currblock));
       }
       DSObj.bChain.InsertBlock_Honest(currblock);
       temp+=1;
    }

  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    int count=DSObj.bChain.tr_count,temp=0,k=0,latestcoin=99999;
    int n=coinCount/count;
    Members mode=new Members();
    mode.UID="Moderator";
    while(temp<n){
      Transaction[] block=new Transaction[count];
      for(int i=0;i<count;i++){
        Transaction newcoin=new Transaction();
        latestcoin+=1;
        DSObj.latestCoinID=String.valueOf(latestcoin);
        newcoin.coinID=DSObj.latestCoinID;
        if(k==DSObj.memberlist.length){
          k=0;
        }
        newcoin.Destination=DSObj.memberlist[k];
        k+=1;
        newcoin.Source=mode;
        block[i]=newcoin;

      }
      TransactionBlock currblock=new TransactionBlock(block);
      for(Transaction i : block){
        i.Destination.mycoins.add(new Pair<>(i.coinID,currblock));
       }
       DSObj.bChain.InsertBlock_Malicious(currblock);
       temp+=1;
    }
  }
}
